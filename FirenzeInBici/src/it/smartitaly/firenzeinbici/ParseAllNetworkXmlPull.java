package it.smartitaly.firenzeinbici;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Xml.Encoding;

import com.google.android.maps.GeoPoint;


public class ParseAllNetworkXmlPull extends AsyncTask<String, String, String> {
	
	private Network _network;
	private InputStream _definitionFile;
	private ArrayList<ArrayList<GeoPoint>> _parsed_paths = new ArrayList<ArrayList<GeoPoint>>(); 

	public ParseAllNetworkXmlPull(Network network, InputStream definitionFile){
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
		_network.onRoutesParsed(_parsed_paths);
		super.onPostExecute(result);
	}

	public void parseXML(InputStream definitionFile){
//		try {
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			factory.setNamespaceAware(true);
//			XmlPullParser xpp = factory.newPullParser();
//			
//			xpp.setInput(definitionFile,"UTF-8");
//			int eventType = xpp.getEventType();
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//		          if(eventType == XmlPullParser.START_DOCUMENT) {
//		              System.out.println("Start document");
//		          } else if(eventType == XmlPullParser.START_TAG) {
//		              System.out.println("Start tag "+xpp.getName());
//		          } else if(eventType == XmlPullParser.END_TAG) {
//		              System.out.println("End tag "+xpp.getName());
//		          } else if(eventType == XmlPullParser.TEXT) {
//		              System.out.println("Text "+xpp.getText());
//		          }
//		          try {
//					eventType = xpp.next();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			
//		} catch (XmlPullParserException e1) {
//			e1.printStackTrace();
//		}
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
		for (int i = 0; i < nList.getLength(); i++){
			ArrayList<GeoPoint> path = new ArrayList<GeoPoint>();
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
		    				path.add(returnGeo(appl[a]));
						}
					}
				}
    		  	_parsed_paths.add(path);
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
