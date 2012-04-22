package it.smartitaly.firenzeinbici;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Environment;

import com.google.android.maps.GeoPoint;


public class ParseAllNetwork extends AsyncTask<String, String, String> {
	
	private Network _network;
	private File _definitionFile;
	private ArrayList<Route> _parsedroutes = new ArrayList<Route>(); 

	public ParseAllNetwork(Network network, File definitionFile){
		_definitionFile = definitionFile;
		_network = network;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		parseXML(_definitionFile);
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		_network.onRoutesParsed(_parsedroutes);
		super.onPostExecute(result);
	}

	public void parseXML(File definitionFile){
		
		NodeList nList = null;
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(definitionFile);
			doc.getDocumentElement().normalize();
	 
			//List of "Placemark" nodes
			nList = doc.getElementsByTagName("MultiGeometry");
			
			//Extract coordinates from "Placemarks" nodes
			extractCoordinates(nList);
            
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	public void extractCoordinates(NodeList nList){
		Route newroute = new Route(null,null,null,null,0);
		for (int i = 0; i < nList.getLength(); i++){
			ArrayList<GeoPoint> newsegment = new ArrayList<GeoPoint>();
			Node nNodeExt = nList.item(i);
			if (nNodeExt.getNodeType() == Node.ELEMENT_NODE){
				Element ePlacemarks = (Element) nNodeExt;
				//List of "Point" nodes
				String point = getTagValue("coordinates",ePlacemarks);
				GeoPoint center;
				center= returnGeo(point);
				
				//List of "LineString" nodes
				NodeList nCoordinates = ePlacemarks.getElementsByTagName("LineString");
				for (int j = 0; j < nCoordinates.getLength(); j++){
					Node nNodeInt = nCoordinates.item(j);
					if (nNodeInt.getNodeType() == Node.ELEMENT_NODE){
						Element eCoordinates = (Element) nNodeInt;
						String[] appl = getTagValue("coordinates",eCoordinates).split("[ ]");
		    		  	for (int a = 0; a < appl.length; a++){
		    				newsegment.add(returnGeo(appl[a]));
						}
		    		  	newroute = new Route(null,null,center,newsegment,0);
					}
				}
    		  	_parsedroutes.add(newroute);
			}
		}
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    return nValue.getNodeValue();
	}
	
	
	public static GeoPoint returnGeo(String point){
		String[] geo = point.split("[,]");
		double latitude = Double.parseDouble(geo[1]) * 1000000;
		int officiallatitude = new BigDecimal (latitude, new MathContext(9)).intValue();
		double longitude = Double.parseDouble(geo[0]) * 1000000;
		int officiallongitude = new BigDecimal (longitude, new MathContext(9)).intValue();
		GeoPoint geopoint = new GeoPoint(officiallatitude,officiallongitude);
		return geopoint;
	}
	
}
