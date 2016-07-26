package com.adobeslash.pokeutils;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

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
	final static Logger logger = Logger.getLogger(PokeHelper.class);

	public static void lootNearestPokestop(PokemonGo go) throws LoginFailedException, RemoteServerException{
		Collection<Pokestop> stops = go.getMap().getMapObjects().getPokestops();
		for(Pokestop ps : stops){
			if(ps.inRange() && ps.canLoot()){
				ps.loot();
				logger.info("A pokestop has been visited");
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
						logger.info(p.getPokemonId().name() + " has been transfered because of low cp : " +p.getCp());
						p.transferPokemon();
					}else{
						logger.info("Pokemon added to the collection, cp : " + p.getCp());
					}
					logger.info("Candy for " + p.getPokemonId().name()+ " : " +p.getCandy() + " ("+p.getCandiesToEvolve()+" to evolve)");					
					found = true;
					break;
				}
			}
			if(!found){
				logger.error("Impossible to retrieve the pokemon in the bag...");
			}else{
				tracer.addPokemon(pokemon.getPokemonId().name());
			}
		}else{
			logger.info("New Entry in the pokedex : " + pokemon.getPokemonId().name());
		}
	}
}
