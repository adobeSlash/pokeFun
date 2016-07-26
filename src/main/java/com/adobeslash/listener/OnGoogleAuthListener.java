package com.adobeslash.listener;

import org.apache.log4j.Logger;

import com.adobeslash.pokeutils.PokeStats;
import com.pokegoapi.auth.GoogleAuthJson;
import com.pokegoapi.auth.GoogleAuthTokenJson;
import com.pokegoapi.auth.GoogleCredentialProvider.OnGoogleLoginOAuthCompleteListener;

public class OnGoogleAuthListener implements OnGoogleLoginOAuthCompleteListener{
	
	final static Logger logger = Logger.getLogger(OnGoogleAuthListener.class);
	private GoogleAuthTokenJson googleAuthTokenJson = null;

	public OnGoogleAuthListener() {
		// TODO Auto-generated constructor stub
	}

	public void onInitialOAuthComplete(GoogleAuthJson googleAuthJson) {
		// TODO Auto-generated method stub
		
	}

	public void onTokenIdReceived(GoogleAuthTokenJson googleAuthTokenJson) {
		logger.info("Token result : " + googleAuthTokenJson);
		this.googleAuthTokenJson = googleAuthTokenJson;
	}
	
	public GoogleAuthTokenJson getRereshToken(){
		return googleAuthTokenJson;
	}
}
