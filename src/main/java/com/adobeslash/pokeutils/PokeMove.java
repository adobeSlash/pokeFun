package com.adobeslash.pokeutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

public class PokeMove extends Thread {
	final static Logger logger = Logger.getLogger(PokeMove.class);
	public PokemonGo go;
	private String itineraire = null;

	public PokeMove(PokemonGo go) {
		this.go = go;
	}

	public PokeMove(PokemonGo go, String itineraire) {
		this.go = go;
		this.itineraire = itineraire;
	}

	public void run() {
		if (itineraire == null)
			try {
				runPokestop();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
			try {
				runItineraire();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void runItineraire() throws ParserConfigurationException, SAXException, IOException {
		List<double[]> coordinates = new KmlParser().getCoordinatesFromKml(itineraire);
		logger.info("nombre de point  : " + coordinates.size());
		while (true) {
			int i = 0;
			for (; i < coordinates.size() - 1; i++) {
				double[] nextPos = coordinates.get(i);
				moveTo(nextPos[0], nextPos[1]);
			}
			for (; i > 0; i--) {
				double[] nextPos = coordinates.get(i);
				moveTo(nextPos[0], nextPos[1]);
			}
		}

	}

	public void runPokestop() throws InterruptedException {
		while (true) {
			try {
				ArrayList<Pokestop> lesPokestops = new ArrayList<Pokestop>(
						(ArrayList<Pokestop>) go.getMap().getMapObjects().getPokestops());
				// Longitude,Latitude
				List<double[]> itineraire = new KmlParser()
						.getCoordinatesFromKml("src/main/resources/itineraireTest.xml");

				Pokestop nearest = null;
				Double distance = null;
				List<Pokestop> lesPokestopALeure = new LinkedList<Pokestop>();
				for (Pokestop p : lesPokestops) {
					if (p.canLoot()) {
						if (p.hasLurePokemon()) {
							lesPokestopALeure.add(p);
							logger.info("Y'a un leurre ici mec!");
						}
						p.loot();
						logger.info("Looter le pokestop " + p.getDetails().getName());

					} else {
						if (p.canLoot(true)) {
							double distanceTmp = distance(lesPokestops.get(0).getLatitude(),
									lesPokestops.get(0).getLongitude(), go.getLatitude(), go.getLongitude());
							if (nearest == null) {
								nearest = p;
								distance = distanceTmp;
							} else {
								distance = distanceTmp < distance ? distanceTmp : distance;
								nearest = distanceTmp < distance ? p : nearest;
							}
						}
					}
				}
				while (lesPokestopALeure.size() > 0) {
					logger.info("OMG ya un leurre je reste ");
					List<Pokestop> aSupr = new LinkedList<Pokestop>();
					long minTime = Long.MAX_VALUE;

					for (Pokestop p : lesPokestopALeure) {
						if (p.canLoot())
							p.loot();
						if (!p.hasLurePokemon())
							aSupr.add(p);
						minTime = Math.min(p.getCooldownCompleteTimestampMs(), minTime);
					}
					lesPokestopALeure.removeAll(aSupr);
					if (lesPokestopALeure.size() > 0 && minTime != Long.MAX_VALUE) {
						Thread.sleep(Math.max(minTime - System.currentTimeMillis(), 0));
					}
				}
				moveTo(nearest);

			} catch (LoginFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public double distance(double lat1, double lon1, double lat2, double lon2) {
		double radians = Math.PI / 180;
		double R = 6371; // km
		double dLat = (lat2 - lat1) * radians;
		double dLon = (lon2 - lon1) * radians;
		lat1 = lat1 * radians;
		lat2 = lat2 * radians;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c * 1000;// en metre sinon en km
	}

	public void moveTo(Pokestop p) throws LoginFailedException, RemoteServerException {
		// bouger 4 m par seconde pour que les pas soient compatibilisé
		logger.info("aller au pokestop  : " + p.getDetails().getName() + " : "
				+ (int) distance(go.getLatitude(), p.getLongitude(), p.getLatitude(), p.getLongitude()) + " metres");
		moveTo(p.getLongitude(), p.getLatitude());
	}

	public void moveTo(double lon, double lat) {
		double meters = 6;
		double inRadian = (180 / Math.PI) * (meters / 6378137);
		// longitude avant car se sert de la latitude
		while (distance(lat, go.getLongitude(), lat, lon) > 12) {

			if (go.getLongitude() < lon)
				go.setLongitude(go.getLongitude() + inRadian / Math.cos(go.getLatitude()));
			else
				go.setLongitude(go.getLongitude() - inRadian / Math.cos(go.getLatitude()));
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (distance(go.getLatitude(), lon, lat, lon) > 12) {

			if (go.getLatitude() < lat)
				go.setLatitude(go.getLatitude() + (180 / Math.PI) * (meters / 6378137));
			else
				go.setLatitude(go.getLatitude() - (180 / Math.PI) * (meters / 6378137));
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
