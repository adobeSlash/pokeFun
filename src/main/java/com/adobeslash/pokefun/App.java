package com.adobeslash.pokefun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;

import com.adobeslash.listener.OnGoogleAuthListener;
import com.adobeslash.pokeutils.PokeHelper;
import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.PokestopLootResult;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleAuthJson;
import com.pokegoapi.auth.GoogleAuthTokenJson;
import com.pokegoapi.auth.GoogleCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	System.out.println("Hello");
 
    	PokeStats tracer = new PokeStats();

    	OkHttpClient httpClient = new OkHttpClient(); 
    	
    	//Proxy worldline...
//		OkHttpClient httpClient = new OkHttpClient.Builder()
//		    .connectTimeout(60, TimeUnit.SECONDS)
//		    .writeTimeout(60, TimeUnit.SECONDS)
//		    .readTimeout(60, TimeUnit.SECONDS)
//		    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();
    		
		try {
			OnGoogleAuthListener authListener = new OnGoogleAuthListener();
			
			PokemonGo go = new PokemonGo(new GoogleCredentialProvider(httpClient, authListener),httpClient);
			
			//go.setLocation( 48.8086335, 2.1335094999999455, 0); //Maison
			//go.setLocation(48.80962619260876, 2.134148, 0); //Gare RD
			//go.setLocation(48.8615963, 2.289282299999968, 0); // Parc du trocadero
			//go.setLocation(48.856181844312594, 2.2977787494903623, 0); // Eiffel
			//go.setLocation(48.863492, 2.327494, 0); // Jardin des Tuileries
			go.setLocation(48.892416, 2.393335, 0); // La vilette
			System.out.println("location : " + go.getLatitude() + "-"
					+ go.getLongitude() + "-"
					+ go.getAltitude() + "-");
			System.out.println("profile : " + go.getPlayerProfile().getUsername());
			
			while(true){
				Thread.sleep(120000);
				System.out.println("scanned");
				ArrayList <CatchablePokemon> catchables = (ArrayList<CatchablePokemon>) go.getMap().getCatchablePokemon();
				for(CatchablePokemon pokemon : catchables){
					System.out.println("catchables : " + pokemon.getPokemonId());
					EncounterResult encResult = pokemon.encounterPokemon();
					// if encounter was succesful, catch
					if (encResult.wasSuccessful()) {
						System.out.println("Encounted:" + pokemon.getPokemonId());
						CatchResult result = pokemon.catchPokemon();
						System.out.println("Attempt to catch:" + pokemon.getPokemonId() + " " + result.getStatus());
						if(result.getStatus().equals(CatchStatus.CATCH_SUCCESS)){
							PokeHelper.askForTransfer(go, pokemon, tracer);
						}
					}else{
						System.out.println("failed to catch pokemon");
					}
				}
				
				PokeHelper.lootNearestPokestop(go);			
			}
		} catch (LoginFailedException e) {
			e.printStackTrace();
		} catch (RemoteServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
