package com.adobeslash.webservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobeslash.listener.OnGoogleAuthListener;
import com.adobeslash.pokefun.PokemonGoFarmerBot;
import com.adobeslash.pokeutils.PokeMove;
import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

//TODO ajouter quelques service
	//les infos sur les pokemon capturé pendant le run #PokeStats
	//redemarrer le service
	//position actuel
	//changer l'itinerraire
	//lancer une evolution pour un pokemon
	//Amount of pokemon doesn't update
@RestController
public class PokeFunController {
	
	private PokemonGoFarmerBot farmer = null;
	private PokemonGo go = null;
	private String token = "REPLACE WITH TOKEN";
	
	@RequestMapping("/liveStats")
    public LiveStats getLiveStats() throws LoginFailedException, RemoteServerException {
		if(go!= null){
			return new LiveStats(go);
		}
        return null;
    }
	
	@RequestMapping("/start")
    public LiveStats startFarmerBot() throws LoginFailedException, RemoteServerException, InterruptedException {
		
		OkHttpClient httpClient = new OkHttpClient(); 
    	OnGoogleAuthListener authListener;
    	
    	//Proxy worldline...
//			OkHttpClient httpClient = new OkHttpClient.Builder()
//			    .connectTimeout(60, TimeUnit.SECONDS)
//			    .writeTimeout(60, TimeUnit.SECONDS)
//			    .readTimeout(60, TimeUnit.SECONDS)
//			    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();
    	
    	authListener = new OnGoogleAuthListener();		
    	GoogleCredentialProvider gcp = new GoogleCredentialProvider(httpClient, token);
		go = new PokemonGo(gcp,httpClient);
		go.setLocation(48.863492, 2.327494, 0);
		
		farmer = new PokemonGoFarmerBot(go);
		farmer.start();
		
		PokeMove pm = new PokeMove(go);
		pm.start();
		
		return new LiveStats(go);
    }

}
