package com.adobeslash.webservice;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobeslash.listener.GoogleAutoCredentialProvider;
import com.adobeslash.listener.OnGoogleAuthListener;
import com.adobeslash.pokefun.PokemonGoFarmerBot;
import com.adobeslash.pokeutils.PokeMove;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

//TODO ajouter quelques service
	//les infos sur les pokemon capturé pendant le run #PokeStats
	//redemarrer le service
	//position actuel
	//changer l'itinerraire
	//lancer une evolution pour un pokemon
	//Amount of pokemon doesn't update directly...latence
@RestController
public class PokeFunController {
	
	final static Logger logger = Logger.getLogger(PokeFunController.class);
	
	private PokemonGoFarmerBot farmer = null;
	private PokeMove pm = null;
	private PokemonGo go = null;
	
	@CrossOrigin
	@RequestMapping("/liveStats")
    public LiveStats getLiveStats() throws LoginFailedException, RemoteServerException {
		if(go!= null){
			return new LiveStats(go);
		}
        return null;
    }
	
	@CrossOrigin
	@RequestMapping("/start")
    public LiveStats startFarmerBot() throws LoginFailedException, RemoteServerException, InterruptedException {
		
		OkHttpClient httpClient = new OkHttpClient(); 
    	
    	//Proxy worldline...
//			OkHttpClient httpClient = new OkHttpClient.Builder()
//			    .connectTimeout(60, TimeUnit.SECONDS)
//			    .writeTimeout(60, TimeUnit.SECONDS)
//			    .readTimeout(60, TimeUnit.SECONDS)
//			    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();
 
    	GoogleAutoCredentialProvider gcp = new GoogleAutoCredentialProvider(httpClient,
    			"slashtutoriel@gmail.com", "NOP");
		go = new PokemonGo(gcp,httpClient);
		go.setLocation(48.863492, 2.327494, 0);
		
		farmer = new PokemonGoFarmerBot(go);
		farmer.start();
		
		pm = new PokeMove(go);
		pm.start();
		
		logger.info("Service started, you are now connected");
		return new LiveStats(go);
    }

}
