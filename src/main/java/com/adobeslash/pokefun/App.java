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
    	String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBiZDEwY2JmMDM2OGQ2MWE0NDBiZjYxZjNiM2EyZDI0NGExODQ5NDcifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdF9oYXNoIjoiUWhseVlGQlRBcW0xNThUX2tjSWRTUSIsImF1ZCI6Ijg0ODIzMjUxMTI0MC03M3JpM3Q3cGx2azk2cGo0Zjg1dWo4b3RkYXQyYWxlbS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODA3OTQ1ODU2NTY4NDkyMzg3NSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI4NDgyMzI1MTEyNDAtNzNyaTN0N3Bsdms5NnBqNGY4NXVqOG90ZGF0MmFsZW0uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6InNsYXNodHV0b3JpZWxAZ21haWwuY29tIiwiaWF0IjoxNDY5NTE0NDk1LCJleHAiOjE0Njk1MTgwOTV9.P4QPor8Vd4ZY9aeiSsSYOwnwFVzvgvwD6TqyO_wOlF5X4t8VUuFwCg_l66FXpMnah2juhe0wQIVnTbJTQZzmU2xizBZlM6InEYeyxyYWbtM1PmPXR0cbaoMyWrwIMhq-ngojv2d6BYlXNc-7EX0bLOZEsy9g1LMsB0zHj_fevZmM6xLZZZRu_NiRi-h4J2ETJnQPlLtFNhcoU0q7uyV9SpPfmU6g5CwUOrCpAqpjgqeM_cLwKlqjVcYk8tllmRq2m4_2rCZyFRP8--ME_3YQuZq5F1GJWTaKLcpVxFWaP4EGqEGPHn7uHHesK130sXmHEMbck9Y09mhBvHPcowRKDQ";
 	
		try {
			AuthInfo auth = new GoogleLogin(httpClient).login(token); 
			//AuthInfo auth = new GoogleLogin(httpClient).login("slashtutoriel@gmil.com","c@stor__88"); 
			PokemonGo go = new PokemonGo(auth,httpClient);
			//go.setLocation( 48.8086335, 2.1335094999999455, 0); //Maison
			go.setLocation(48.80962619260876, 2.134148, 0); //Gare RD
			//go.setLocation(48.8615963, 2.289282299999968, 0); // Parc du trocadero
			//go.setLocation(48.856181844312594, 2.2977787494903623, 0); // Eiffel
			System.out.println("location : " + go.getLatitude() + "-"
					+ go.getLongitude() + "-"
					+ go.getAltitude() + "-");
			System.out.println("profile : " + go.getPlayerProfile().getUsername());
			while(true){
				Thread.sleep(5000);
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
							long pokemonId = result.getCapturedPokemonId();
							List<Pokemon> pkl =
									go.getInventories().getPokebank().getPokemons();
							Boolean found = false;
							for(Pokemon p : pkl){
								if(p.getPokemonId() == pokemon.getPokemonId()){
									//TODO DO NOT DELETE FIRST CATCHED POKEMON
									if(p.getCp() < 250){
										System.out.println(p.getNickname() + " has been transfered because of low cp : " 
												+p.getCp());
										p.transferPokemon();
									}else{
										System.out.println("Pokemon added to the collection, cp : " + p.getCp());
									}
									mail.sendMailForCapture(pokemon.getPokemonId().name(), 
											go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount(),p.getCp());
									found = true;
									break;
								}
							}
							if(!found){
								System.out.println("Impossible to retrieve the pokemon in the bag...");
							}
						}
					}else{
						System.out.println("failed to catch pokemon");
					}
				}
			}
		} catch (LoginFailedException e) {
			e.printStackTrace();
			mail.sendMailLoginException(e);
		} catch (RemoteServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
