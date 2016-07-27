package com.adobeslash.webservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobeslash.pokefun.PokemonGoFarmerBot;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

@RestController
public class PokeFunController {
	
	private PokemonGoFarmerBot farmer = null;

	@RequestMapping("/liveStats")
    public LiveStats getLiveStats() throws LoginFailedException, RemoteServerException {
		if(farmer != null){
			return new LiveStats(farmer.go);
		}
        return null;
    }
	
	@RequestMapping("/start")
    public LiveStats startFarmerBot() throws LoginFailedException, RemoteServerException, InterruptedException {
		if(farmer == null){
			farmer = new PokemonGoFarmerBot();
			farmer.runBot();
		}
		return new LiveStats(farmer.go);
    }
	
	@RequestMapping("/test")
    public String test() {
		return "test";
    }

}
