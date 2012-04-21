package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;
import it.smartitaly.firenzeinbici.overlays.RouteOverlay;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyRouteMapActivity extends MapActivity {
	
	private Route _route;
	MapView mapview;
	ArrayList<GeoPoint> myroutepoints = new ArrayList<GeoPoint>();
	Button btninfo;
	WebView info;
	List<Overlay> listOfOverlays;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		mapview = (MapView)findViewById(R.id.map);
        mapview.setBuiltInZoomControls(true);
        
        btninfo = (Button)findViewById(R.id.btninfo);
        btninfo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (info.getVisibility() == View.GONE){
					info.setVisibility(View.VISIBLE);
				} else {
					info.setVisibility(View.GONE);
				}
			}
        	
        });
        
        GlobalState gs = (GlobalState)getApplication();
        _route = gs.getActiveRoute();

        
        info = (WebView)findViewById(R.id.webinfo);
        info.loadData(_route.getDescription(), "text/html", null);
        
        RouteOverlay myOverlay = new RouteOverlay(_route,mapview);
        mapview.getOverlays().add(myOverlay);
        myOverlay.centerMap();
    }
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}