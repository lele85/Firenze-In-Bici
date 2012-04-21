package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.overlays.NetworkOverlay;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class AllNetworkMapActivity extends MapActivity {

	MapView mapview;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.networkmap);
		
		mapview = (MapView)findViewById(R.id.map);
		
		GlobalState gs = (GlobalState)getApplication();
		NetworkOverlay networkoverlay = new NetworkOverlay(gs.getNetwork(),mapview);
		mapview.getOverlays().add(networkoverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
