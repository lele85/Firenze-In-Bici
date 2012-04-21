package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RouteSelectionActivity extends MapActivity {

	//ArrayList<Route> myroutes = new ArrayList<Route>();
		MapView mapview;
		List<Overlay> listOfOverlays;
		ListView listroutes;
		List<Route> finalroutes = new ArrayList<Route>();
		
	    /** Called when the activity is first created. */
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.listroutes);
	        
	        mapview = (MapView)findViewById(R.id.map);
	        
	        listroutes = (ListView)findViewById(R.id.listroutes);
	        
	        finalroutes = Route.getAll(Environment.getExternalStorageDirectory() + 
	        		"/unzipped/routes.xml");

	        ArrayList<HashMap<String,Object>> myroutes = new ArrayList<HashMap<String,Object>>();
	        
	        for (Route myroute : finalroutes){
	        	HashMap<String,Object> newroute = new HashMap<String,Object>();
	        	newroute.put("name", myroute.getName());
	        	newroute.put("description", myroute.getDescription());
	        	newroute.put("image", myroute.getPhoto());
	        	myroutes.add(newroute);
	        }
	        
	        String[] from = {"name","description","image"};
	        int[] to = {R.id.routename,R.id.routenumber,R.id.routeimage};
	        
	        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),myroutes,R.layout.routeitem,from,to);
	        listroutes.setAdapter(adapter);
	        
	        listroutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int selected_index, long arg3) {
						GlobalState gs = (GlobalState)getApplication();
						
						Intent i = new Intent().setClass(getApplicationContext(), MyRouteMapActivity.class);
						gs.setActiveRoute(finalroutes.get(selected_index));
				        startActivity(i);
				}
			});
	    }
		
		
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
