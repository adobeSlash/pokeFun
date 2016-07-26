package com.adobeslash.pokefun;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.LoginException;

import com.pokegoapi.exceptions.LoginFailedException;

public class SendMailTLS {
	
	private final String MAIL_SUBJECT = "PokemonGO";
	private final String CONFIG_PATH = "src/main/config/user.properties";
	private final String CONFIG_USR = "mail";
	private final String CONFIG_MDP = "mail.mdp";
	
	private String username = "";
	private String password = "";
	
	private Properties prop = new Properties();
	private InputStream input = null;
	
	private Session session;

	public SendMailTLS() {
		
		initUserInfo();
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
	}
	
	public void sendMailForCapture(String pokemonName, int pokeballNumber){
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(username));
			message.setSubject(MAIL_SUBJECT);
			//TODO StringBuilder
			message.setText("Pokemon catched : " + pokemonName + "/n "
					+ "Remaining pokeball : " + pokeballNumber);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendMailLoginException(LoginFailedException e){
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(username));
			message.setSubject("Login exception");
			//TODO StringBuilder
			message.setText("You got an exception : " + e.getLocalizedMessage());

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void initUserInfo(){
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(CONFIG_PATH);
			prop.load(input);
			
			username = prop.getProperty(CONFIG_USR);
			password = prop.getProperty(CONFIG_MDP);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
