package com.adobeslash.pokeutils;

import java.util.HashMap;
import java.util.Iterator;

import com.pokegoapi.api.PokemonGo;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

public class PokeStats {
	
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
		System.out.println("--------------------------- START STATS -----------------------");
		while(it.hasNext()){
			Object cle = it.next(); 
			Object value = catched.get(cle); 
			System.out.println("Id : " + cle + " nb catched : " + value);
			if(go != null){
				System.out.println("pokeball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_POKE_BALL).getCount());
				System.out.println("superball amount : " + go.getInventories().getItemBag().getItem(ItemId.ITEM_GREAT_BALL).getCount());
			}
		}
		System.out.println("--------------------------- END STATS -----------------------");
	}
}
