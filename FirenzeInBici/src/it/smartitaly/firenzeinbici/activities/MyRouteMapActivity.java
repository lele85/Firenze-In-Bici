package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.Fountain;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.OverlayManager;
import it.smartitaly.firenzeinbici.OverlayType;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;
import it.smartitaly.firenzeinbici.overlays.RouteOverlay;
import it.smartitaly.firenzeinbici.overlays.FountainsOverlay;

import java.util.EnumMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyRouteMapActivity extends MapActivity {
	
	private Route _route;
	MapView mapview;
	Button btninfo, btnpdi;
	WebView info;
	List<Fountain> fountains;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		mapview = (MapView)findViewById(R.id.map);
        mapview.setBuiltInZoomControls(true);
        
        
        GlobalState gs = (GlobalState) getApplication();
        fountains = gs.getFountains();
        gs.getOverlayManager().applyActiveOverlays(mapview);
        
        Drawable icon = getResources().getDrawable(R.drawable.markerfontanella);
        
        btnpdi = (Button)findViewById(R.id.btnpdi);
        btnpdi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				CharSequence[] items = {"Rastrelliere","Fontanelle","Negozi di bici","Affitto Bici"};
				boolean[] initialitemschecked = {false,false,false,false};
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MyRouteMapActivity.this);
				builder.setTitle("Punti di interesse");
				
				builder.setMultiChoiceItems(items, initialitemschecked, new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						Log.i("info",Integer.toString(which));
						
					}
				});
				
				final AlertDialog alert = builder.create();
				
				alert.setButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alert.cancel();
					}
				});
				alert.show();
			}
        	
        });
        
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
        
        //GlobalState gs = (GlobalState)getApplication();
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