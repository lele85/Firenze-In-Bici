package it.smartitaly.firenzeinbici;

import java.util.List;

import com.google.android.maps.GeoPoint;

public class DistanceCalculator {
	

	private static final double Radius = 6371;

	// R = earth’s radius (mean radius = 6,371km)

	// Mi da la distanza in kilometri tra due geopoint
	public static double calculateDistance(GeoPoint StartP, GeoPoint EndP) {
		double lat1 = StartP.getLatitudeE6() / 1E6;
		double lat2 = EndP.getLatitudeE6() / 1E6;
		double lon1 = StartP.getLongitudeE6() / 1E6;
		double lon2 = EndP.getLongitudeE6() / 1E6;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Radius * c;
	}
	
	public static double findDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
    }
	
	public static double calculateLenght(List<GeoPoint> path){
		double lenght = 0;
		if (path.size() != 0){
			GeoPoint lastPoint = path.get(0);
			for (GeoPoint currentPoint : path) {
				lenght += DistanceCalculator.calculateDistance(lastPoint, currentPoint);
				lastPoint = currentPoint;
			}
		}
		return lenght;
	}

}
