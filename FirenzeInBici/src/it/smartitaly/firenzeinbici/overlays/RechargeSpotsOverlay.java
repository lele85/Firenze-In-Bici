package it.smartitaly.firenzeinbici.overlays;

import it.smartitaly.firenzeinbici.RechargeSpot;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class RechargeSpotsOverlay extends ItemizedOverlay<OverlayItem> {

	private List<RechargeSpot> _rechargeSpots = new ArrayList<RechargeSpot>();
	private List<OverlayItem> _items = new ArrayList<OverlayItem>();
	private Context _context;
	
	public RechargeSpotsOverlay(Drawable defaultMarker,
			List<RechargeSpot> rechargeSpots, Context context) {
		super(boundCenterBottom(defaultMarker));
		_rechargeSpots = rechargeSpots;
		_context = context;
		setLastFocusedIndex(-1);
		updateItems();
	}

	private static Rect _currentMapBoundsRect = new Rect();
	private static Point _itemToDrawPointInScreenCoordinates = new Point();
	
	private void updateItems(){
		for (RechargeSpot rechargeSpot : _rechargeSpots) {
			OverlayItem i = new OverlayItem(rechargeSpot.getLocation(), "titolo", "snippet");
			_items.add(i);
		}
		populate();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow){
		/*
		mapView.getDrawingRect(_currentMapBoundsRect);
		for (Fountain fountain : _fountains) {
			mapView.getProjection().toPixels(fountain.getLocation(),
					_itemToDrawPointInScreenCoordinates);
			if (isCurrentLocationVisible(_itemToDrawPointInScreenCoordinates,
					_currentMapBoundsRect)) {
				drawSpot(canvas, _itemToDrawPointInScreenCoordinates);
			}
		}*/
		super.draw(canvas, mapView, shadow);
		}
	}

	private static Paint _circlePaint = new Paint();

	private static void drawSpot(Canvas canvas, Point point) {
		_circlePaint.setColor(android.graphics.Color.BLUE);
		_circlePaint.setStyle(Paint.Style.FILL);
		_circlePaint.setAlpha(35);
		canvas.drawCircle(point.x, point.y, 20.0f, _circlePaint);
	}

	private static boolean isCurrentLocationVisible(
			Point pointInScreenCoordinates, Rect currentMapBoundsRect) {
		return currentMapBoundsRect.contains(pointInScreenCoordinates.x,
				pointInScreenCoordinates.y);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return _items.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return _items.size();
	}
	
	//@Override
	/*protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		AlertDialog.Builder b = new Builder(_context);
		b.setMessage("Alerta").create().show();
		return super.onTap(index);
	}*/
	
}