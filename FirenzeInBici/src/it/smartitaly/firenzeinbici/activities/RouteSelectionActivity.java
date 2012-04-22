package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.AppPaths;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

		finalroutes = Route.getAll(_globalState.getAppPaths().getFile(
				AppPaths.Resources.ROUTES_FILE));

		ArrayList<HashMap<String, Object>> myroutes = new ArrayList<HashMap<String, Object>>();

		for (Route myroute : finalroutes) {
			HashMap<String, Object> newroute = new HashMap<String, Object>();
			newroute.put("name", myroute.getName());
			newroute.put("description", myroute.getDescription());
			newroute.put("image", myroute.getThumb(_globalState.getAppPaths()));
			myroutes.add(newroute);
		}

		String[] from = { "name", "description", "image" };
		int[] to = { R.id.routename, R.id.routenumber, R.id.routeimage };


		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),myroutes, R.layout.routeitem, from, to);
		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (data instanceof Bitmap) {
					Log.i("PATHS", "bitmap");
					Bitmap thumb = (Bitmap) data;
					ImageView imageView = (ImageView) view;
					imageView.setImageBitmap(thumb);
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

	private void initGlobalState() {
		_globalState = (GlobalState) getApplication();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
