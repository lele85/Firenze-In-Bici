package it.smartitaly.firenzeinbici.overlays;

import it.smartitaly.firenzeinbici.Fountain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class FountainsOverlay extends Overlay {

	private List<Fountain> _fountains = new ArrayList<Fountain>();

	public FountainsOverlay(Drawable defaultMarker,
			List<Fountain> fountains, Context context) {
		super();
		_fountains = fountains;
	}

	private static Rect _currentMapBoundsRect = new Rect();
	private static Point _itemToDrawPointInScreenCoordinates = new Point();

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		//Log.i("PARSER",_points.toString());
		mapView.getDrawingRect(_currentMapBoundsRect);
		for (Fountain fountain : _fountains) {
			mapView.getProjection().toPixels(fountain.getLocation(),
					_itemToDrawPointInScreenCoordinates);
			if (isCurrentLocationVisible(_itemToDrawPointInScreenCoordinates,
					_currentMapBoundsRect)) {
				drawSpot(canvas, _itemToDrawPointInScreenCoordinates);
			}
		}
	}

	private static Paint _circlePaint = new Paint();

	private static void drawSpot(Canvas canvas, Point point) {
		_circlePaint.setColor(android.graphics.Color.RED);
		_circlePaint.setStyle(Paint.Style.FILL);
		_circlePaint.setAlpha(35);
		canvas.drawCircle(point.x, point.y, 20.0f, _circlePaint);
	}

	private static boolean isCurrentLocationVisible(
			Point pointInScreenCoordinates, Rect currentMapBoundsRect) {
		return currentMapBoundsRect.contains(pointInScreenCoordinates.x,
				pointInScreenCoordinates.y);
	}
	
}
