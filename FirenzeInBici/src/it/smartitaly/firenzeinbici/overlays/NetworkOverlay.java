package it.smartitaly.firenzeinbici.overlays;

import java.util.ArrayList;

import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.Route;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class NetworkOverlay extends Overlay {

	private Network _network;
	MapView map;
	
	public NetworkOverlay(Network network, MapView map){
		_network = network;
		this.map = map;
	}
	
	@Override
    public void draw(Canvas canvas, MapView mv, boolean shadow) {
            super.draw(canvas, mv, shadow);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            for (ArrayList<GeoPoint> path : _network.getPaths()){
            	int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
            	for (GeoPoint geopoint: path) {
            		Point point = new Point();
                	mv.getProjection().toPixels(geopoint, point);
                    x2 = point.x;
                    y2 = point.y;
                    if ((x1 != -1) && (y1 != -1)) {
                      	canvas.drawLine(x1, y1, x2, y2, paint);
                    }
                    x1 = x2;
                    y1 = y2;	
				}
            }
    }
	
	/*public void centerMap(){
		MapController mapcontroller = map.getController();
		GeoPoint maxLat = ManageGeoPoints.returnMaxGeoLat(myroutepoints);
		GeoPoint minLat = ManageGeoPoints.returnMinGeoLat(myroutepoints);
		GeoPoint maxLong = ManageGeoPoints.returnMaxGeoLong(myroutepoints);
		GeoPoint minLong = ManageGeoPoints.returnMinGeoLong(myroutepoints);
		
		mapcontroller.setCenter(ManageGeoPoints.getRouteCenter(maxLat,minLat,maxLong,minLong));
		mapcontroller.zoomToSpan(maxLat.getLatitudeE6()-minLat.getLatitudeE6(), 
				maxLong.getLongitudeE6()-minLong.getLongitudeE6());
	}*/ 

}