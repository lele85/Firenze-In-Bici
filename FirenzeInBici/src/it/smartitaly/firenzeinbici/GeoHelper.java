package it.smartitaly.firenzeinbici;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.android.maps.GeoPoint;

public class GeoHelper {

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
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Radius * c;
	}

	public static double findDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

	public static double calculateLenght(List<GeoPoint> path) {
		double lenght = 0;
		if (path.size() != 0) {
			GeoPoint lastPoint = path.get(0);
			for (GeoPoint currentPoint : path) {
				lenght += GeoHelper.calculateDistance(lastPoint,
						currentPoint);
				lastPoint = currentPoint;
			}
		}
		return lenght;
	}
	
	// Calcola l'involucro convesso di un set di geopoint
	public static List<GeoPoint> computeConvexHull(Set<GeoPoint> points) {
		if (points == null) {
			throw new IllegalArgumentException();
		}

		if (points.size() == 1) {
			return new ArrayList<GeoPoint>(points);
		}

		// Path list
		ArrayList<GeoPoint> path = new ArrayList<GeoPoint>();

		// Pick the min longitude point as first
		GeoPoint first = null;
		for (GeoPoint point : points) {
			if (first == null
					|| point.getLongitudeE6() < first.getLongitudeE6()) {
				first = point;
			}
		}

		if (first != null) {
			path.add(first);
		} else {
			throw new IllegalArgumentException(
					"Can't retrieve the western point");
		}

		boolean right = true;
		boolean loop = true;

		while (loop) {

			GeoPoint current = path.get(path.size() - 1);

			GeoPoint next = null;
			double nextLatitudeDiff = 0;
			double nextLongitudeDiff = 0;
			boolean recordNext = false;
			for (GeoPoint point : points) {
				if (!point.equals(current)) {
					double latitudeDiff = point.getLatitudeE6()
							- current.getLatitudeE6();
					double longitudeDiff = point.getLongitudeE6()
							- current.getLongitudeE6();

					if (longitudeDiff == 0 && latitudeDiff < 0 == right
							|| longitudeDiff > 0 == right) {
						if (next == null) {
							recordNext = true;
						} else {
							// Compare the 'a' in the linear equation Y = a.X
							// that correspond to the line slope.
							boolean negativeSlope = latitudeDiff
									* nextLongitudeDiff - nextLatitudeDiff
									* longitudeDiff < 0;

							if (negativeSlope) {
								recordNext = true;
							}
						}

						if (recordNext) {
							recordNext = false;
							next = point;
							nextLatitudeDiff = latitudeDiff;
							nextLongitudeDiff = longitudeDiff;
						}

					}
				}
			}

			if (next != null) {

				// Check abnormal case
				if (path.size() > points.size() + 1) {
					throw new RuntimeException("Convex hull computation failed");
				}

				path.add(next);

				/*
				 * If we are not back to the first point, let's continue finding
				 * the path.
				 * 
				 * Otherwise, do nothing => going back from the stack, this is
				 * actually the end point of the recursive algorithm.
				 */

				if (path.get(0) == next) {
					loop = false;
				}

			} else {
				// We have reach the point on the right, let's go back!
				right = !right;
			}
		}
		return path;
	}

}
