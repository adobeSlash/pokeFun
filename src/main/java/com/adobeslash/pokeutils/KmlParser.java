package com.adobeslash.pokeutils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KmlParser {
	
	 public static List<String> getCoordinatesFromKml(String path) throws ParserConfigurationException, SAXException, IOException {

			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);					
			doc.getDocumentElement().normalize();
					
			NodeList coordinatesList = doc.getElementsByTagName("coordinates");			

			Node coordonnees = coordinatesList.item(0);
			String positions = coordonnees.getTextContent();
			
			List<String> positionsList = Arrays.asList(positions.split(" "));
			List<String> parsedList = new ArrayList<String>();
			String[] parser;
			
			for (String coord : positionsList) {
							
				parser = coord.split(",");
				String longLat = parser[0]+ ',' + parser[1];
				parsedList.add(longLat);				
			}
						
			return parsedList;	
		
		    } 

}
