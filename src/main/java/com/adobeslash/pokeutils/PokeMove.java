package com.adobeslash.pokeutils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

//TODO change french logs
public class PokeMove extends Thread {
  final static Logger logger = Logger.getLogger(PokeMove.class);
  public PokemonGo go;
  
  public PokeMove(PokemonGo go){
	  this.go = go;
  }

  public void run() {
    while (true) {
      try {
    	ArrayList<Pokestop> lesPokestops = new ArrayList<Pokestop>((ArrayList<Pokestop>) go.getMap().getMapObjects().getPokestops());
        
        Pokestop nearest = null;
        Double distance = null;
        for (Pokestop p : lesPokestops) {
          if (p.canLoot()) {
              try{
            p.loot();
                        } catch (Exception e) {
                  logger.info("sac plein");
              }
            logger.info("Looter le pokestop " + p.getDetails().getName());
          } else {
            if (p.canLoot(true)) {
              double distanceTmp = distance(lesPokestops.get(0).getLatitude(), lesPokestops.get(0).getLongitude(), go.getLatitude(), go.getLongitude());
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
        moveTo(nearest);

      } catch (LoginFailedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (RemoteServerException e) {
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

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000;// en metre sinon en km
  }

  public void moveTo(Pokestop p) throws LoginFailedException, RemoteServerException {
        // bouger 4 m par seconde pour que les pas soient compatibilisé
	  logger.info("aller au pokestop  : " + p.getDetails().getName() + " : " + 
    (int) distance(go.getLatitude(), p.getLongitude(), p.getLatitude(), p.getLongitude()) + " metres");

    double meters = 2;
    double inRadian = (180 / Math.PI) * (meters / 6378137);
    // longitude avant car se sert de la latitude
    while (distance(p.getLatitude(), go.getLongitude(), p.getLatitude(), p.getLongitude()) > 12) {

      if (go.getLongitude() < p.getLongitude())
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
    logger.info("aller au pokestop  longitude OK: " + p.getDetails().getName());
    while (distance(go.getLatitude(), p.getLongitude(), p.getLatitude(), p.getLongitude()) > 12) {

      if (go.getLatitude() < p.getLatitude())
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
