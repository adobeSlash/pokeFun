package com.adobeslash.pokeutils;

import java.util.HashMap;
import java.util.Iterator;

public class PokeStats {
	
	private HashMap<String, Integer> catched;
	private int totalAmount;

	public PokeStats(){
		catched = new HashMap<String, Integer>();
		totalAmount = 0;
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
		System.out.println("--------------------------- SATRT STATS -----------------------");
		while(it.hasNext()){
			Object cle = it.next(); // tu peux typer plus finement ici
			Object value = catched.get(cle); // tu peux typer plus finement ici
			System.out.println("Id : " + cle + " nb catched : " + value);
		}
		System.out.println("--------------------------- END STATS -----------------------");
	}
}
