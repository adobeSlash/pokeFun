package com.adobeslash.pokefun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;

import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.PokestopLootResult;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.api.pokemon.Pokemon;
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
    	String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjUwNzgyYmNmMGE5NzQxZTZiZjkwMjY2ZGMzNTY4YWE5MDc5MWYxNmYifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdF9oYXNoIjoiTXRxZmJQN0t6ai1yN2JHT0tseC1oQSIsImF1ZCI6Ijg0ODIzMjUxMTI0MC03M3JpM3Q3cGx2azk2cGo0Zjg1dWo4b3RkYXQyYWxlbS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODA3OTQ1ODU2NTY4NDkyMzg3NSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI4NDgyMzI1MTEyNDAtNzNyaTN0N3Bsdms5NnBqNGY4NXVqOG90ZGF0MmFsZW0uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6InNsYXNodHV0b3JpZWxAZ21haWwuY29tIiwiaWF0IjoxNDY5NTUxMjQzLCJleHAiOjE0Njk1NTQ4NDN9.IkrbESWfm_9_jg48z5wpe5UZexmv1r8qbrwFPFPuM-Qg4tyQiqAMG02wTON_TpE1j7Cu7pmFl5gBRbwGaABUKVe1WXE2ETkwr3eCKUQYLTZZZmaxtlum5lH8tOZUtowsyADoPplo3UB8OB7FkZhPjhLQUwNV4uEmInkGdDQ368AIgdXxnR_q7bHPRKsP2gBcnpa6pgRP7vQcCj-Wxd8UArShfQYrSnvRCaBtqfnmAbvTy7qU3qxiergMyY-qQ9y-2hjAMys9wXtruEpwRzul5NDbjNb_50s5sfGP6Ewnd6vTMksaDUqQOR7tjN-tHll3Ls9ervB21v9KyFAyGbTkwg";

    	OkHttpClient httpClient = new OkHttpClient(); 
    	
    	//Proxy worldline...
//		OkHttpClient httpClient = new OkHttpClient.Builder()
//		    .connectTimeout(60, TimeUnit.SECONDS)
//		    .writeTimeout(60, TimeUnit.SECONDS)
//		    .readTimeout(60, TimeUnit.SECONDS)
//		    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();
    		
		try {
			
			PokemonGo go = new PokemonGo(new GoogleCredentialProvider(httpClient,token),httpClient);
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
							Boolean found = false;
							if(go.getInventories().getPokedex().getPokedexEntry(pokemon.getPokemonId()).getTimesCaptured() > 2){
								List<Pokemon> pkl =
										go.getInventories().getPokebank().getPokemons();
								for(Pokemon p : pkl){
									if(p.getPokemonId() == pokemon.getPokemonId()){
										if(p.getCp() < 350){
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
								}else{
									tracer.addPokemon(pokemon.getPokemonId().name());
								}
							}else{
								System.out.println("New Entry in the pokedex : " + pokemon.getPokemonId().name());
							}
						}
					}else{
						System.out.println("failed to catch pokemon");
					}
				}
				
				Collection<Pokestop> stops = go.getMap().getMapObjects().getPokestops();
				for(Pokestop ps : stops){
					if(ps.inRange() && ps.canLoot()){
						int prevAmount = go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount();
						PokestopLootResult result = ps.loot();
						List<ItemAward> awards = result.getItemsAwarded();
						//TODO add awards
						int nextAmount = go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount();
						System.out.println("Pokestop looted");
						if(prevAmount < nextAmount){
							System.out.println("You got some pokeballs");
						}
					}
				}				
			}
		} catch (LoginFailedException e) {
			e.printStackTrace();
		} catch (RemoteServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
