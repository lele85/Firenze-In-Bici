package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RouteSelectionActivity extends MapActivity {

	// ArrayList<Route> myroutes = new ArrayList<Route>();
	MapView mapview;
	List<Overlay> listOfOverlays;
	ListView listroutes;
	List<Route> finalroutes = new ArrayList<Route>();
	GlobalState _globalState;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		initGlobalState();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listroutes);

		mapview = (MapView) findViewById(R.id.map);

		listroutes = (ListView) findViewById(R.id.listroutes);

		finalroutes = Route.getAll(getResources().openRawResource(R.raw.routes));

		ArrayList<HashMap<String, Object>> myroutes = new ArrayList<HashMap<String, Object>>();

		for (Route myroute : finalroutes) {
			HashMap<String, Object> newroute = new HashMap<String, Object>();
			newroute.put("route_name_and_composition", myroute.getName() + " - Ciclabile al " +myroute.getCyclabilePercentage() + "%" );
			newroute.put("route_distance_and_time", getTimeAndDistanceLabelText(myroute.getLenght(),myroute.getTravelTimeInMinutes()));
			newroute.put("route_description", myroute.getDescription());
			newroute.put("routeimage", myroute.getThumb(this));
			myroutes.add(newroute);
		}

		String[] from = { "route_name_and_composition", "route_description", "routeimage", "route_distance_and_time" };
		int[] to = {R.id.route_name_and_composition, R.id.route_description, R.id.routeimage, R.id.route_distance_and_time };


		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),myroutes, R.layout.routeitem, from, to);
		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (data instanceof Drawable) {
					Drawable thumb = (Drawable) data;
					ImageView imageView = (ImageView) view;
					imageView.setImageDrawable(thumb);
					return true;
				}
				return false;
			}
		});

		listroutes.setAdapter(adapter);

		listroutes
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int selected_index, long arg3) {
						Intent i = new Intent().setClass(
								getApplicationContext(),
								MyRouteMapActivity.class);
						_globalState.setActiveRoute(finalroutes
								.get(selected_index));
						startActivity(i);
					}
				});
	}
	
	private String getTimeAndDistanceLabelText(double distance, int minutes){
	        DecimalFormat df = new DecimalFormat("#.##");
	        return df.format(distance) + "Km  " + minutes + " min";
	}

	private void initGlobalState() {
		_globalState = (GlobalState) getApplication();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.getParent().onBackPressed();
	}

}
