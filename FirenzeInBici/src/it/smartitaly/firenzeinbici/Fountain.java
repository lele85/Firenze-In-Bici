package it.smartitaly.firenzeinbici;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.XmlResourceParser;

import com.google.android.maps.GeoPoint;

public class Fountain implements GeoLocalized {
	private String name;
	private String description;
	private GeoPoint place;
	
	public static List<Fountain> getAll(InputStream definitionFile){
		NodeList nList;
		ArrayList<Fountain> fountains = new ArrayList<Fountain>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(definitionFile);
			doc.getDocumentElement().normalize();
			
			nList = doc.getElementsByTagName("Placemark");
			for (int i = 0; i < nList.getLength(); i++){
				Element eNode = (Element) nList.item(i);
				
				String name = getTagValue("name",eNode);
				String description = getTagValue("description",eNode);
				GeoPoint geopoint = ManageGeoPoints.returnGeo(getTagValue("coordinates",eNode));
				
				
				Fountain fountain = new Fountain(
						name, 
						description, 
						geopoint);
				
				fountains.add(fountain);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fountains;
	}

	public Fountain (String name, String description, GeoPoint place){
		this.name = name;
		this.description = description;
		this.place = place;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public GeoPoint getPlace(){
		return place;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    return nValue.getNodeValue();
	}

	@Override
	public GeoPoint getLocation() {
		return place;
	}
}
