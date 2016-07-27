package com.adobeslash.webservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobeslash.pokefun.PokemonGoFarmerBot;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

@RestController
public class PokeFunController {
	
	private PokemonGoFarmerBot farmer = null;
	//TODO ajouter quelques service
	//les infos sur les pokemon capturé pendant le run #PokeStats
	//redemarrer le service
	//position actuel
	//changer l'itinerraire
	//lancer une evolution pour un pokemon

	//TODO mettre a jour les stats
	@RequestMapping("/liveStats")
    public LiveStats getLiveStats() throws LoginFailedException, RemoteServerException {
		if(farmer != null){
			return new LiveStats(farmer.go);
		}
        return null;
    }
	
	//TODO renvoyer le code pour se logger
	@RequestMapping("/start")
    public LiveStats startFarmerBot() throws LoginFailedException, RemoteServerException, InterruptedException {
		if(farmer == null){
			farmer = new PokemonGoFarmerBot();
			farmer.runBot();
		}
		return new LiveStats(farmer.go);
    }

}
