package com.adobeslash.listener;

import org.apache.log4j.Logger;

import com.pokegoapi.auth.GoogleAuthJson;
import com.pokegoapi.auth.GoogleAuthTokenJson;
import com.pokegoapi.auth.GoogleCredentialProvider.OnGoogleLoginOAuthCompleteListener;

public class OnGoogleAuthListener implements OnGoogleLoginOAuthCompleteListener{
	
	final static Logger logger = Logger.getLogger(OnGoogleAuthListener.class);
	private String googleAuthTokenJson = null;

	public void onInitialOAuthComplete(GoogleAuthJson googleAuthJson) {
		logger.info("Waiting for the code " + googleAuthJson.getUserCode() + 
				" to be put in " + googleAuthJson.getVerificationUrl());
		googleAuthTokenJson = googleAuthJson.getUserCode();
	}

	//TODO writte the token into a file
	public void onTokenIdReceived(GoogleAuthTokenJson googleAuthTokenJson) {
		//TODO save this locally to futur usage
		logger.info("token obtained : " + googleAuthTokenJson.getAccessToken());
	}
	
	public String getAuthToken(){
		return googleAuthTokenJson;
	}
}
