package com.adobeslash.pokefun;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.adobeslash.pokeutils.PokeHelper;
import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;

public class PokemonGoFarmerBot extends Thread {

  final static Logger logger = Logger.getLogger(PokemonGoFarmerBot.class);
  public PokemonGo go;

  public PokemonGoFarmerBot(PokemonGo go) {
    this.go = go;
  }

  public void run() {

    try {

      PokeStats tracer = PokeStats.getInstance(go);

      logger.info("location : " + go.getLatitude() + "-"
        + go.getLongitude() + "-"
        + go.getAltitude() + "-");
      logger.info("profile : " + go.getPlayerProfile().getPlayerData().getUsername());

      while (true) {

        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          logger.error(e.getLocalizedMessage());
          return;
        }
        logger.info("start scan...");

        ArrayList<CatchablePokemon> catchables = (ArrayList<CatchablePokemon>) go.getMap().getCatchablePokemon();
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

        // PokeHelper.lootNearestPokestop(go);
        logger.info("end of scan...");
      }
    } catch (LoginFailedException e) {
      logger.error(e.getLocalizedMessage());
      return;

    } catch (RemoteServerException e) {
      logger.error(e.getLocalizedMessage());
      return;
    } catch (NoSuchItemException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
