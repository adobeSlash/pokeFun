package com.adobeslash.pokeutils;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.pokegoapi.api.PokemonGo;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

public class PokeStats {
	
	final static Logger logger = Logger.getLogger(PokeStats.class);
	
	private HashMap<String, Integer> catched;
	private int totalAmount;
	private PokemonGo go = null;

	public PokeStats(){
		catched = new HashMap<String, Integer>();
		totalAmount = 0;
	}
	
	public PokeStats(PokemonGo go){
		catched = new HashMap<String, Integer>();
		totalAmount = 0;
		this.go = go;
	}
	
	public void addPokemon(String name){
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
	
	public void printStats(){
		Iterator it = catched.keySet().iterator();
		logger.info("--------------------------- START STATS -----------------------");
		while(it.hasNext()){
			Object cle = it.next(); 
			Object value = catched.get(cle); 
			logger.info("Id : " + cle + " nb catched : " + value);
			if(go != null){
				logger.info("pokeball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount());
				logger.info("superball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_GREAT_BALL).getCount());
			}
		}
		logger.info("--------------------------- END STATS -----------------------");
	}
}
