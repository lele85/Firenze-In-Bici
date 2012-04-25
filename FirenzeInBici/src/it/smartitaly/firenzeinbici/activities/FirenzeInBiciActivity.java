package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.AppPaths;
import it.smartitaly.firenzeinbici.BikeRack;
import it.smartitaly.firenzeinbici.Fountain;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.OverlayManager;
import it.smartitaly.firenzeinbici.OverlayType;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;
import it.smartitaly.firenzeinbici.overlays.FountainsOverlay;

import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.List;

import com.google.android.maps.Overlay;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class FirenzeInBiciActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host_layout);

		AppPaths paths = null;
		try {
			paths = new AppPaths(
					"FirenzeInBici",
					"routes.xml",
					"ciclabili-percorsi_ciclabili.kml",
					"rastrelliere.kml",
					"img",
					"fontanelli.kml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

		final Network network = new Network(
				paths.getFile(AppPaths.Resources.ALL_NETWORK_FILE));
		
		List<Fountain> fountains = Fountain.getAll(paths.getFile(AppPaths.Resources.FOUNTAINS_FILE));
		List<BikeRack> racks = BikeRack.getAll(paths.getFile(AppPaths.Resources.RACKS_FILE));
		
		EnumMap<OverlayType, Boolean> globalOverlayStatus = new EnumMap<OverlayType, Boolean>(OverlayType.class);
		globalOverlayStatus.put(OverlayType.FONTANELLE, new Boolean(true));
		globalOverlayStatus.put(OverlayType.RASTRELLIERE, new Boolean(true));
		
		
		// Set global state (Quick and dirty hack)
		GlobalState globalState = (GlobalState) getApplication();
		globalState.setAppPaths(paths);
		globalState.setFountains(fountains);
		globalState.setBikeRacks(racks);
		globalState.setOverlayStatus(globalOverlayStatus);
		
		final TabHost tabHost = getTabHost();
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		Intent routeSelectionIntent = new Intent().setClass(this,
				RouteSelectionActivity.class);
		Intent allNetworkIntent = new Intent().setClass(this,
				AllNetworkMapActivity.class);
		setupTab(new TextView(this), "Suggeriti", tabHost, routeSelectionIntent, R.layout.tab_layout);
		setupTab(new TextView(this), "Rete", tabHost, allNetworkIntent, R.layout.async_tab_layout);
		setupTab(new TextView(this), "Segnala", tabHost, allNetworkIntent, R.layout.tab_layout);

		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);

		OnNetworkDataAvailableListener listener = new OnNetworkDataAvailableListener() {

			@Override
			public void OnNetworkDataAvailable() {
				tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(true);
				GlobalState gs = (GlobalState) getApplication();
				gs.setNetwork(network);
				ProgressBar allnetworkProgressBar =(ProgressBar)findViewById(R.id.progressBar1);
				allnetworkProgressBar.setVisibility(View.GONE);
			}
		};
		network.setOnDataAvailableListener(listener);
		network.load();
	}

	private void setupTab(final View view, final String title, TabHost tabHost,
			Intent targetIntent, int layoutId) {
		View tabview = createTabView(tabHost.getContext(), title,layoutId);
		TabSpec spec = tabHost.newTabSpec(title).setIndicator(tabview)
				.setContent(targetIntent);
		tabHost.addTab(spec);
	}

	private static View createTabView(final Context context, final String text, int layoutId) {
		View view = LayoutInflater.from(context).inflate(layoutId,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}