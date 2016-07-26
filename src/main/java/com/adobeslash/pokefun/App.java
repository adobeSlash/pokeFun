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
    	String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBiZDEwY2JmMDM2OGQ2MWE0NDBiZjYxZjNiM2EyZDI0NGExODQ5NDcifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdF9oYXNoIjoidVNwWUVxMEhGWDFPMWJCbTg0cWJYZyIsImF1ZCI6Ijg0ODIzMjUxMTI0MC03M3JpM3Q3cGx2azk2cGo0Zjg1dWo4b3RkYXQyYWxlbS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODA3OTQ1ODU2NTY4NDkyMzg3NSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI4NDgyMzI1MTEyNDAtNzNyaTN0N3Bsdms5NnBqNGY4NXVqOG90ZGF0MmFsZW0uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6InNsYXNodHV0b3JpZWxAZ21haWwuY29tIiwiaWF0IjoxNDY5NTEwMDM5LCJleHAiOjE0Njk1MTM2Mzl9.Et5UvHejGvadenq2DQij3dARozKMMJzh4U3dPMMtZz4bRa6jX17lp4Ecn6Jiu_41RdjlH7cgM9HgDR-QYO9Lp8MZxdCHXzEUO_n-skD_T6Z_PDfW9riyu7iwAi8z_-323Lr5cZPuj6zWdvhN10SIGYB-pztU9OV4qozFxzB1JL_hrHlW-eLss8RceTkvmnzVBf1DGcrY5PCwmZWMTlKFsWpEcY9KjlFzZSpA8r_rC_Pz0KbfsrGpemKrUBNMsMOgZ4Ez5rmO_zho2eFO9nOL_k117yR1tHZaCcro73FhX18GDdz-gHsOjGfTiH_41woNKEQQRZsPuTPEbVBj1Th-xg";
    	
		try {
			AuthInfo auth = new GoogleLogin(httpClient).login(token); 
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
			mail.sendMailLoginException(e);
			main(null);
		} catch (RemoteServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
