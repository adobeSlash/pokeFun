package com.adobeslash.pokeutils;

import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

public abstract class PokeHelper {

	public static void clearNoFavoriteFromBag(PokemonGo go) throws LoginFailedException, RemoteServerException{
		List<Pokemon> pkl =
				go.getInventories().getPokebank().getPokemons();
		for(Pokemon pokemon : pkl){
			if(!pokemon.getFavorite()){
				pokemon.transferPokemon();
			}
		}
	}

}
