package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainTabActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host_layout);
		
		final TabHost tabHost = getTabHost();
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		Intent routeSelectionIntent = new Intent().setClass(this,
				RouteSelectionActivity.class);
		Intent allNetworkIntent = new Intent().setClass(this,
				AllNetworkMapActivity.class);
		setupTab(new TextView(this), "Percorsi Suggeriti", tabHost, routeSelectionIntent, R.layout.tab_layout);
		setupTab(new TextView(this), "Tutta La Rete Ciclabile", tabHost, allNetworkIntent, R.layout.tab_layout);
		setupTab(new TextView(this), "Proponi il tuo percorso", tabHost, allNetworkIntent, R.layout.tab_layout);

		tabHost.setCurrentTab(0);

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
