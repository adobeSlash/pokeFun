package com.adobeslash.webservice;

import java.util.HashMap;

import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

/**
 * 
 * @author adobe
 * Class use to send live stats through web Service
 */
public class LiveStats {
	
	private String username;
	private int level;
	private int itemCount;
	private int pokeball;
	private int superball;
	private int hyperball;
	private int amountOfPokemon;
	
	private Boolean isFarmerAlive;
	private Boolean isPokeMoveAlive;
	
	private HashMap<String, Integer> catched;
	
	public LiveStats(){
		
	}
	
	public LiveStats(final PokemonGo go) throws LoginFailedException, RemoteServerException {
		updateStats(go);
	}
	
	public LiveStats(final PokemonGo go, Boolean isFarmerAlive, Boolean isPokeMoveAlive) throws LoginFailedException, RemoteServerException {
		updateStats(go);
		this.isFarmerAlive = isFarmerAlive;
		this.isPokeMoveAlive = isPokeMoveAlive;
	}
	
	public void updateStats(final PokemonGo go) throws LoginFailedException, RemoteServerException{
		go.getInventories().updateInventories();
		this.username = go.getPlayerProfile().getUsername();
		this.level = go.getPlayerProfile().getStats().getLevel();
		this.itemCount = go.getInventories().getItemBag().getItemsCount();
		this.pokeball = go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount();
		this.superball = go.getInventories().getItemBag().getItem(ItemId.ITEM_GREAT_BALL).getCount();
		this.hyperball = go.getInventories().getItemBag().getItem(ItemId.ITEM_ULTRA_BALL).getCount();
		this.amountOfPokemon = go.getInventories().getPokebank().getPokemons().size();
		setCatched(PokeStats.getInstance(go).getCatched());
	}

	public String getUsername() {
		return username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemStorage) {
		this.itemCount = itemStorage;
	}

	public int getPokeball() {
		return pokeball;
	}

	public void setPokeball(int pokeball) {
		this.pokeball = pokeball;
	}

	public int getSuperball() {
		return superball;
	}

	public void setSuperball(int superball) {
		this.superball = superball;
	}

	public int getHyperball() {
		return hyperball;
	}

	public void setHyperball(int hyperball) {
		this.hyperball = hyperball;
	}

	public int getAmountOfPokemon() {
		return amountOfPokemon;
	}

	public void setAmountOfPokemon(int amountOfPokemon) {
		this.amountOfPokemon = amountOfPokemon;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public HashMap<String, Integer> getCatched() {
		return catched;
	}

	public void setCatched(HashMap<String, Integer> catched) {
		this.catched = catched;
	}

	public Boolean getIsPokeMoveAlive() {
		return isPokeMoveAlive;
	}

	public void setIsPokeMoveAlive(Boolean isPokeMoveAlive) {
		this.isPokeMoveAlive = isPokeMoveAlive;
	}

	public Boolean getIsFarmerAlive() {
		return isFarmerAlive;
	}

	public void setIsFarmerAlive(Boolean isFarmerAlive) {
		this.isFarmerAlive = isFarmerAlive;
	}
	

}
