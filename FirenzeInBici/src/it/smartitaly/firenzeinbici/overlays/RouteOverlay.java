package it.smartitaly.firenzeinbici.overlays;

import it.smartitaly.firenzeinbici.ManageGeoPoints;
import it.smartitaly.firenzeinbici.Route;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RouteOverlay extends Overlay {

	private Route _route;
	MapView map;

	public RouteOverlay(Route route, MapView map) {
		_route = route;
		this.map = map;
	}
	
	private static Point _point = new Point();
	
	@Override
	public void draw(Canvas canvas, MapView mv, boolean shadow) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
		for (GeoPoint geopoint : _route.getCoordinates()) {
			mv.getProjection().toPixels(geopoint, _point);

			x2 = _point.x;
			y2 = _point.y;

			if ((x1 != -1) && (y1 != -1)) {
				canvas.drawLine(x1, y1, x2, y2, paint);
			}

			x1 = x2;
			y1 = y2;
		}
	}

	public void centerMap() {
		MapController mapcontroller = map.getController();
		GeoPoint maxLat = ManageGeoPoints.returnMaxGeoLat(_route
				.getCoordinates());
		GeoPoint minLat = ManageGeoPoints.returnMinGeoLat(_route
				.getCoordinates());
		GeoPoint maxLong = ManageGeoPoints.returnMaxGeoLong(_route
				.getCoordinates());
		GeoPoint minLong = ManageGeoPoints.returnMinGeoLong(_route
				.getCoordinates());

		mapcontroller.setCenter(ManageGeoPoints.getRouteCenter(maxLat, minLat,
				maxLong, minLong));
		mapcontroller.zoomToSpan(
				maxLat.getLatitudeE6() - minLat.getLatitudeE6(),
				maxLong.getLongitudeE6() - minLong.getLongitudeE6());
	}

}
