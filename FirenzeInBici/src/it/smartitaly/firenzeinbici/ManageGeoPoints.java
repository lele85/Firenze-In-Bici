package it.smartitaly.firenzeinbici;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;

public class ManageGeoPoints {
	
	public static List<GeoPoint> returnGeoList(String list){
		ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();
		String[] array = list.split("[ ]");
		for (String geostring : array){
			geopoints.add(returnGeo(geostring));
		}
		return geopoints;
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

	public static GeoPoint returnMaxGeoLat(List<GeoPoint> geopoints){
		GeoPoint maxLat = new GeoPoint(0,0);
		for (GeoPoint verify : geopoints){
			if (verify.getLatitudeE6() > maxLat.getLatitudeE6()){
				maxLat = verify;
			}
		}
		return maxLat;
	}
	
	public static GeoPoint returnMinGeoLat(List<GeoPoint> geopoints){
		GeoPoint minLat = new GeoPoint(90000000,90000000);
		for (GeoPoint verify : geopoints){
			if (verify.getLatitudeE6() < minLat.getLatitudeE6()){
				minLat = verify;
			}
		}
		return minLat;
	}
	
	public static GeoPoint returnMaxGeoLong(List<GeoPoint> geopoints){
		GeoPoint maxLong = new GeoPoint(0,0);
		for (GeoPoint verify : geopoints){
			if (verify.getLongitudeE6() > maxLong.getLongitudeE6()){
				maxLong = verify;
			}
		}
		return maxLong;
	}
	
	public static GeoPoint returnMinGeoLong(List<GeoPoint> geopoints){
		GeoPoint minLong = new GeoPoint(90000000,90000000);
		for (GeoPoint verify : geopoints){
			if (verify.getLongitudeE6() < minLong.getLongitudeE6()){
				minLong = verify;
			}
		}
		return minLong;
	}
	
	public static GeoPoint getRouteCenter(GeoPoint maxLat, GeoPoint minLat, GeoPoint maxLong, GeoPoint minLong){
		
		int latCenter = minLat.getLatitudeE6() + ((maxLat.getLatitudeE6() - minLat.getLatitudeE6()) / 2);
		int longCenter = minLong.getLongitudeE6() + ((maxLong.getLongitudeE6() - minLong.getLongitudeE6()) / 2);
		
		GeoPoint center = new GeoPoint (latCenter,longCenter);
		return center;
	}
	
	public static GeoPoint getRouteCenter(List<GeoPoint> points){
		return getRouteCenter(
				returnMaxGeoLat(points),
				returnMinGeoLat(points),
				returnMaxGeoLong(points),
				returnMinGeoLong(points));
	}
	
}
