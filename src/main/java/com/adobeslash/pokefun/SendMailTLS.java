package com.adobeslash.pokefun;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {
	
	final String username = "LOGIN";
	final String password = "PASSWORD";
	
	Session session;

	public SendMailTLS() {
		
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
			message.setFrom(new InternetAddress("slashtutoriel@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("slashtutoriel@gmail.com"));
			message.setSubject("PokemonGO");
			message.setText("Pokemon catched : " + pokemonName + "/n "
					+ "Remaining pokeball : " + pokeballNumber);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
