package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.AppPaths;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.io.FileNotFoundException;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;


public class FirenzeInBiciActivity extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tablayout);
	    
	    AppPaths paths = null;
		try {
			paths = new AppPaths(
					"FirenzeInBici",
					"routes.xml",
					"ciclabili-percorsi_ciclabili.kml",
					"rastrelliere.kml",
					"img");
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		GlobalState gs = (GlobalState) getApplication();
		gs.setAppPaths(paths);
	    
	    final Network network = new Network(paths.getFile(AppPaths.Resources.ALL_NETWORK_FILE));
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    final TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    intent = new Intent().setClass(this, RouteSelectionActivity.class);

	    spec = tabHost.newTabSpec("suggeriti").setIndicator("Suggeriti",
	                      res.getDrawable(R.drawable.androidmarker))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, AllNetworkMapActivity.class);
	    spec = tabHost.newTabSpec("rete").setIndicator("Tutta la rete",
	                      res.getDrawable(R.drawable.androidmarker))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    

	    tabHost.setCurrentTab(0);
	    tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);
	    
	    OnNetworkDataAvailableListener listener = new OnNetworkDataAvailableListener() {
			
			@Override
			public void OnNetworkDataAvailable() {
				tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(true);
				GlobalState gs = (GlobalState)getApplication();
				gs.setNetwork(network);
			}
		};
		network.setOnDataAvailableListener(listener);
		network.load();
	}
}