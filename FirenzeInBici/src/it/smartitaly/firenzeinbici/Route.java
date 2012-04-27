package it.smartitaly.firenzeinbici;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class Route {
	private String name;
	private String description;
	private GeoPoint center;
	private List<GeoPoint> coordinates;
	private int cyclable_percentage;
	private int street_percentage;
	private int ztl_percentage;
	private String thumb_name;
	private double lenght;
	private int travel_time_minutes;
	
	private double CO2_GRAMS_PER_KM = 127.97;
	private double STANDARD_BIKE_SPEED_KM_PER_HOUR = 9.0;
	private double STANDARD_AUTO_KM_PER_LITRE = 18.0;
	private double STANDARD_GASOLINE_EURO_PER_LITRE = 1.9;
	private double METS_CYCLYNG_SLOW = 4;
	private double HUMAN_WEIGHT_KG = 70;
	private double KCALS_IN_ONE_GRAM = 7.71617917647;
	
	public static List<Route> getAll(InputStream definitionFile){
		
		NodeList nList;
		ArrayList<Route> routes = new ArrayList<Route>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(definitionFile);
			doc.getDocumentElement().normalize();
			
			nList = doc.getElementsByTagName("route");
			
			for (int i = 0; i < nList.getLength(); i++){
				Element eNode = (Element) nList.item(i);
				
				String name = getTagValue("name",eNode);
				String description = getTagValue("description",eNode);
				List<GeoPoint> geopoints = ManageGeoPoints.returnGeoList(getTagValue("coordinates",eNode));
				int percentage_cyclable = Integer.parseInt(getTagValue("percentage_cyclable",eNode));
				int percentage_street = Integer.parseInt(getTagValue("percentage_street",eNode));
				int percentage_ztl = Integer.parseInt(getTagValue("percentage_ztl",eNode));
				String thumb_name = getTagValue("thumb_name",eNode);
				GeoPoint center = ManageGeoPoints.getRouteCenter(geopoints);
				
				Route route = new Route(
						name,
						description,
						center,
						geopoints,
						thumb_name,
						percentage_cyclable);
				routes.add(route);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return routes;
	}
	
	public Route(String name,
			String description,
			GeoPoint center,
			List<GeoPoint> coordinates,
			String thumbName,
			int percentage_cyclable){
		this.name = name;
		this.description = description;
		this.center = center;
		this.coordinates = coordinates;
		this.thumb_name = thumbName;
		this.lenght = GeoHelper.calculateLenght(coordinates);
		this.travel_time_minutes = getMinutes(lenght, STANDARD_BIKE_SPEED_KM_PER_HOUR);
		this.cyclable_percentage = percentage_cyclable;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public GeoPoint getCenter(){
		return center;
	}
	
	public int getCyclabilePercentage(){
		return cyclable_percentage;
	}
	
	public double getLenght(){
		return lenght;
	}
	
	public double getCo2SavedInGrams(){
		return lenght*CO2_GRAMS_PER_KM;
	}
	
	public double getCo2SavedInKiloGrams(){
		return lenght*CO2_GRAMS_PER_KM/1000;
	}
	
	public double getGasolineSavedInLitre(){
		return lenght/STANDARD_AUTO_KM_PER_LITRE;
	}
	
	public double getGasolineSavedInEuro(){
		return getGasolineSavedInLitre()*STANDARD_GASOLINE_EURO_PER_LITRE;
	}
	
	public int getTravelTimeInMinutes(){
		return  travel_time_minutes;
	}
	
	public List<GeoPoint> getCoordinates(){
		return coordinates;
	}
	
	public String getThumb(){
		return thumb_name;
	}
	
	public double getGramsOfFatBurned(){
		return getBurnedKcal()/KCALS_IN_ONE_GRAM;
	}
	
	public double getBurnedKcal(){
		double lostKCals = METS_CYCLYNG_SLOW * HUMAN_WEIGHT_KG * getTravelTimeInMinutes()/60;
		return lostKCals;
	}
	
	// Speed km/h
	// Len: km
	private int getMinutes(double lenght, double speed){
		return (int) Math.round((lenght/speed)*60);
	}
	
	public Drawable getThumb(Context context){
		Resources res = context.getResources();
		int drawableId = context.getResources().getIdentifier(thumb_name, "drawable", "it.smartitaly.firenzeinbici");
		Drawable drawable = res.getDrawable(drawableId);
        return drawable;
	}
	
	
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    return nValue.getNodeValue();
	}
}
