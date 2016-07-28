package com.adobeslash.pokefun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.adobeslash.listener.GoogleAutoCredentialProvider;
import com.adobeslash.listener.OnGoogleAuthListener;
import com.adobeslash.pokeutils.KmlParser;
import com.adobeslash.pokeutils.PokeHelper;
import com.adobeslash.pokeutils.PokeMove;
import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import okhttp3.OkHttpClient;

public class App {
  final static Logger logger = Logger.getLogger(App.class);

  public static void main(String[] args)
    throws InterruptedException, ParserConfigurationException, SAXException, IOException {

    // OkHttpClient httpClient = new OkHttpClient();
    OnGoogleAuthListener authListener;

    // Proxy worldline...
    OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
      .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();

    try {
      GoogleAutoCredentialProvider gcp = new GoogleAutoCredentialProvider(httpClient, Constante.login, Constante.pwd);
      // gcp.refreshToken(token);
      PokemonGo go = new PokemonGo(gcp, httpClient);
      PokeStats tracer = new PokeStats(go);
      String itineraire = "src/main/resources/itineraireTest.xml";
      // go.setLocation( 48.8086335, 2.1335094999999455, 0); //Maison
      // go.setLocation(48.80962619260876, 2.134148, 0); //Gare RD
      double[] start = new KmlParser().getCoordinatesFromKml(itineraire).get(0);
      go.setLocation(start[1], start[0], 0);
      // go.setLocation(48.8615963, 2.289282299999968, 0); // Parc du trocadero
      // go.setLocation(48.856181844312594, 2.2977787494903623, 0); Eiffel
      // go.setLocation(48.863492, 2.327494, 0); // Jardin des Tuileries
      // go.setLocation(48.892416, 2.393335, 0); // La vilette
      logger.info("location : " + go.getLatitude() + "-" + go.getLongitude() + "-" + go.getAltitude() + "-");
      logger.info("profile : " + go.getPlayerProfile().getUsername());

      PokeMove pm = new PokeMove(go, itineraire);
      pm.start();

      while (true) {

        Thread.sleep(10000);
        logger.info("start scan...");

        ArrayList<CatchablePokemon> catchables = (ArrayList<CatchablePokemon>) go.getMap()
          .getCatchablePokemon();
        for (CatchablePokemon pokemon : catchables) {
          logger.info("catchables : " + pokemon.getPokemonId());

          EncounterResult encResult = pokemon.encounterPokemon();

          if (encResult.wasSuccessful()) {
            logger.info("Encounted:" + pokemon.getPokemonId());
            CatchResult result = pokemon.catchPokemon();
            logger.info("Attempt to catch:" + pokemon.getPokemonId() + " " + result.getStatus());

            if (result.getStatus().equals(CatchStatus.CATCH_SUCCESS)) {
              PokeHelper.askForTransfer(go, pokemon, tracer);
            }
          } else {
            logger.info("failed to catch pokemon");
          }
        }

        PokeHelper.lootNearestPokestop(go);
        logger.info("end of scan...");
      }
    } catch (LoginFailedException e) {
      logger.error(e.getLocalizedMessage());

    } catch (RemoteServerException e) {
      logger.error(e.getLocalizedMessage());
    }
  }
}
