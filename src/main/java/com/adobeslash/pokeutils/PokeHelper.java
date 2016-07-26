package com.adobeslash.pokeutils;

import java.util.Collection;
import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.PokestopLootResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

public abstract class PokeHelper {
	
	public final static int REQUIRED_CP = 400;

	public static void lootNearestPokestop(PokemonGo go) throws LoginFailedException, RemoteServerException{
		Collection<Pokestop> stops = go.getMap().getMapObjects().getPokestops();
		for(Pokestop ps : stops){
			if(ps.inRange() && ps.canLoot()){
				ps.loot();
			}
		}
	}
	
	public static void askForTransfer(PokemonGo go, CatchablePokemon pokemon, PokeStats tracer) throws LoginFailedException, RemoteServerException{
		Boolean found = false;
		if(go.getInventories().getPokedex().getPokedexEntry(pokemon.getPokemonId()).getTimesCaptured() > 2){
			List<Pokemon> pkl =
					go.getInventories().getPokebank().getPokemons();
			for(Pokemon p : pkl){
				if(p.getPokemonId() == pokemon.getPokemonId()){
					if(p.getCp() < REQUIRED_CP | p.getPokemonId().equals(PokemonId.DROWZEE)){
						System.out.println(p.getPokemonId().name() + " has been transfered because of low cp : " 
								+p.getCp());
						System.out.println("Candy for " + p.getPokemonId().name()+ " : " +p.getCandy()
								+ " ("+p.getCandiesToEvolve()+" to evolve)");
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
}
