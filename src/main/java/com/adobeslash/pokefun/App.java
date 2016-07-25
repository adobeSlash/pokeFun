package com.adobeslash.pokefun;

import java.util.ArrayList;
import java.util.List;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	System.out.println("Hello");
    	OkHttpClient httpClient = new OkHttpClient(); 
    	SendMailTLS mail= new SendMailTLS();
    	
		try {
			AuthInfo auth = new GoogleLogin(httpClient).login("ADDRESS","MDP"); 
			PokemonGo go = new PokemonGo(auth,httpClient);
			//go.setLocation( 48.8086335, 2.1335094999999455, 0);
			go.setLocation(48.80962619260876, 2.134148, 0);
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
							mail.sendMailForCapture(pokemon.getPokemonId().name(), 
									go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount());
							long pokemonId = result.getCapturedPokemonId();
							List<Pokemon> pkl =
									go.getInventories().getPokebank().getPokemons();
							for(Pokemon p : pkl){
								if(p.getId() == pokemonId){
									if(p.getCp() < 100 && 
											go.getInventories().getPokebank().getPokemonByPokemonId(p.getPokemonId()).size() > 1){
										System.out.println(p.getNickname() + " has been transfered because of low cp : " 
												+p.getCp());
										p.transferPokemon();
									}
									break;
								}
							}
						}
					}else{
						System.out.println("failed to catch pokemon");
					}
				}
			}
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
