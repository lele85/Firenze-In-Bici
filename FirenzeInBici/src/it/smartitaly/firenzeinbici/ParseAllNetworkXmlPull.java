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
		boolean inMultiGeometry = false;
		boolean inLineString = false;
		boolean inCoordinates = false;
		
		ArrayList<GeoPoint> currentPath = null;
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			xpp.setInput(definitionFile,"UTF-8");
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
		          if(eventType == XmlPullParser.START_DOCUMENT) {
		              System.out.println("Start document");
		          } else if(eventType == XmlPullParser.START_TAG) {
		        	  String tagName = xpp.getName();
		        	  if (tagName.equals("MultiGeometry")){
		        		  currentPath = new ArrayList<GeoPoint>();
		        		  inMultiGeometry = true;
		        	  } else if(tagName.equals("LineString")){
		        		  inLineString = true;
		        	  } else if(tagName.equals("coordinates")){
		        		  inCoordinates = true;
		        	  }
		          } else if(eventType == XmlPullParser.END_TAG) {
		        	  String tagName = xpp.getName();
		        	  if (tagName.equals("MultiGeometry")){
		        		  inMultiGeometry = false;
		        	  } else if(tagName.equals("LineString")){
		        		  inLineString = false;
		        		  _parsed_paths.add(currentPath);
		        		  currentPath = null;
		        	  } else if(tagName.equals("coordinates")){
		        		  inCoordinates = false;
		        	  }
		          } else if(eventType == XmlPullParser.TEXT) {
		        	  if (inMultiGeometry && inCoordinates && inLineString){
		        		  String[] strPoints = xpp.getText().split("[ ]");
		        		  for (String strPoint : strPoints) {
		        			  currentPath.add(returnGeo(strPoint));
						}
		              }
		          }
		          try {
					eventType = xpp.next();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
		
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
