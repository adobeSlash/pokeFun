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

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

//TODO ajouter quelques service
	//redemarrer le service
	//position actuel
	//changer l'itinerraire
	//lancer une evolution pour un pokemon
	//Amount of pokemon doesn't update directly...latence
	//Control if aready started in /start
@RestController
public class PokeFunController {
	
	final static Logger logger = Logger.getLogger(PokeFunController.class);
	
	private PokemonGoFarmerBot farmer = null;
	private PokeMove pm = null;
	private PokemonGo go = null;
	private GoogleAutoCredentialProvider gcp = null;
	private OkHttpClient httpClient = null;
	
	@CrossOrigin
	@RequestMapping("/liveStats")
    public LiveStats getLiveStats() throws LoginFailedException, RemoteServerException {
		if(go!= null){
			return new LiveStats(go, farmer.isAlive(), pm.isAlive());
		}
        return null;
    }
	
	@CrossOrigin
	@RequestMapping("/stop")
    public LiveStats stopFarmerBot() throws LoginFailedException, RemoteServerException {
		LiveStats finalS = new LiveStats(go);
		
		pm.interrupt();
		farmer.interrupt();
		//TODO How to disconnect googlAccount ?
		gcp = null;
		go = null;
		httpClient = null;
		
		return finalS;
    }
	
	@CrossOrigin
	@RequestMapping("/start")
    public LiveStats startFarmerBot() throws LoginFailedException, RemoteServerException, InterruptedException {
		
		httpClient = new OkHttpClient(); 
    	
    	//Proxy worldline...
//			OkHttpClient httpClient = new OkHttpClient.Builder()
//			    .connectTimeout(60, TimeUnit.SECONDS)
//			    .writeTimeout(60, TimeUnit.SECONDS)
//			    .readTimeout(60, TimeUnit.SECONDS)
//			    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("prx-dev02", 3128))).build();
 
    	gcp = new GoogleAutoCredentialProvider(httpClient,
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
