package com.adobeslash.pokeutils;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

public class PokeStats {
	
	
	final static Logger logger = Logger.getLogger(PokeStats.class);
	private static PokeStats instance = null;
	
	private HashMap<String, Integer> catched;
	private int totalAmount;
	private PokemonGo go = null;

	protected PokeStats(){
		catched = new HashMap<String, Integer>();
		totalAmount = 0;
	}
	
	protected PokeStats(PokemonGo go){
		catched = new HashMap<String, Integer>();
		totalAmount = 0;
		this.go = go;
	}
	
	public void addPokemon(String name) throws LoginFailedException, RemoteServerException{
		if(catched.containsKey(name)){
			int amount = catched.get(name);
			catched.replace(name, amount + 1);
		}else{
			catched.put(name, 1);
		}
		totalAmount++;
		if(totalAmount % 10 == 0){
			printStats();
		}
	}
	
	public HashMap<String, Integer> getCatched(){
		return catched;
	}
	
	public void printStats() throws LoginFailedException, RemoteServerException{
		Iterator it = catched.keySet().iterator();
		logger.info("--------------------------- START STATS -----------------------");
		while(it.hasNext()){
			Object cle = it.next(); 
			Object value = catched.get(cle); 
			logger.info("Id : " + cle + " nb catched : " + value);
		}
		if(go != null){
			go.getInventories().updateInventories();
			logger.info("pokeball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount());
			logger.info("superball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_GREAT_BALL).getCount());
			logger.info("hyperball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_ULTRA_BALL).getCount());
			logger.info("Km walked : " + go.getPlayerProfile().getStats().getKmWalked());
		}
		logger.info("--------------------------- END STATS -----------------------");
	}
	
	public static PokeStats getInstance(PokemonGo go) {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (PokeStats.class) {
				if (instance == null) {
					instance = new PokeStats(go);
				}
			}
		}
		return instance;
	}

}
