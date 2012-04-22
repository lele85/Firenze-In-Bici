package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.AppPaths;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.io.FileNotFoundException;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class FirenzeInBiciActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host_layout);

		AppPaths paths = null;
		try {
			paths = new AppPaths("FirenzeInBici", "routes.xml",
					"ciclabili-percorsi_ciclabili.kml", "rastrelliere.kml",
					"img");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		GlobalState gs = (GlobalState) getApplication();
		gs.setAppPaths(paths);

		final Network network = new Network(
				paths.getFile(AppPaths.Resources.ALL_NETWORK_FILE));

		final TabHost tabHost = getTabHost();
		Intent routeSelectionIntent = new Intent().setClass(this,
				RouteSelectionActivity.class);
		Intent allNetworkIntent = new Intent().setClass(this,
				AllNetworkMapActivity.class);
		setupTab(new TextView(this), "Suggeriti", tabHost, routeSelectionIntent);
		setupTab(new TextView(this), "Rete", tabHost, allNetworkIntent);

		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);

		OnNetworkDataAvailableListener listener = new OnNetworkDataAvailableListener() {

			@Override
			public void OnNetworkDataAvailable() {
				tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(true);
				GlobalState gs = (GlobalState) getApplication();
				gs.setNetwork(network);
			}
		};
		network.setOnDataAvailableListener(listener);
		network.load();
	}

	private void setupTab(final View view, final String title, TabHost tabHost,
			Intent targetIntent) {
		View tabview = createTabView(tabHost.getContext(), title);
		TabSpec spec = tabHost.newTabSpec(title).setIndicator(tabview)
				.setContent(targetIntent);
		tabHost.addTab(spec);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_layout,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}