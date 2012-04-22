package it.smartitaly.firenzeinbici;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
	
	public static List<Route> getAll(File definitionFile){
		
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
						thumb_name);
				routes.add(route);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return routes;
	}
	
	public Route(String name, String description, GeoPoint center, List<GeoPoint> coordinates, String thumbName){
		this.name = name;
		this.description = description;
		this.center = center;
		this.coordinates = coordinates;
		this.thumb_name = thumbName;
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
	
	public List<GeoPoint> getCoordinates(){
		return coordinates;
	}
	
	public String getThumb(){
		return thumb_name;
	}
	
	public Bitmap getThumb(AppPaths paths){
        File thumb = paths.getImageFile(thumb_name);
        Bitmap bitmap = BitmapFactory.decodeFile(thumb.getAbsolutePath());
        return bitmap;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    return nValue.getNodeValue();
	}
}
