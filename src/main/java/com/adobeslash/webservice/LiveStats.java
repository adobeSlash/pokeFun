package com.adobeslash.webservice;

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
	
	private final String username;
	private int level;
	private int itemStorage;
	private int pokeball;
	private int superball;
	private int hyperball;

	public LiveStats(final PokemonGo go) throws LoginFailedException, RemoteServerException {
		this.username = go.getPlayerProfile().getUsername();
		updateStats(go);
	}
	
	public void updateStats(final PokemonGo go) throws LoginFailedException, RemoteServerException{
		go.getInventories().updateInventories();
		this.level = go.getPlayerProfile().getStats().getLevel();
		this.itemStorage = go.getPlayerProfile().getItemStorage();
		this.pokeball = go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount();
		this.superball = go.getInventories().getItemBag().getItem(ItemId.ITEM_GREAT_BALL).getCount();
		this.hyperball = go.getInventories().getItemBag().getItem(ItemId.ITEM_ULTRA_BALL).getCount();
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

	public int getItemStorage() {
		return itemStorage;
	}

	public void setItemStorage(int itemStorage) {
		this.itemStorage = itemStorage;
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
	

}
