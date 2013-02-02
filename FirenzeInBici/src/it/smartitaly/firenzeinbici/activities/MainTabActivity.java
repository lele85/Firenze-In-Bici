package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

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
		Intent suggestARouteIntent = new Intent().setClass(this,
				SuggestARouteActivity.class);
		setupTab(new TextView(this), "Esempi di Percorsi", tabHost, routeSelectionIntent, R.layout.tab_layout, "ESEMPI");
		setupTab(new TextView(this), "Tutta La Rete Ciclabile", tabHost, allNetworkIntent, R.layout.tab_layout, "ALL_NET");
		setupTab(new TextView(this), "Proponi il tuo percorso", tabHost, suggestARouteIntent, R.layout.tab_layout, "PROPONI");
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			private int previous_tab = 0;
			
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == "ESEMPI"){
					previous_tab = 0;
				}
				if (tabId == "ALL_NET"){
					Toast.makeText(tabHost.getTabWidget().getContext(), "Controlla la tua connessione a internet per visualizzare la mappa", Toast.LENGTH_LONG).show();
					tabHost.setCurrentTab(previous_tab);
					previous_tab = 1;
				}
				if (tabId == "PROPONI"){
					previous_tab = 2;
				}
				
				
			}
		});
		
		tabHost.setCurrentTab(getStartTabIndex());

	}
	
	
	private int getStartTabIndex(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null){
			if (info.isConnected()){
				return 1;
			}
		};
		return 0;
	}

	private void setupTab(final View view, final String title, TabHost tabHost,
			Intent targetIntent, int layoutId, String tag) {
		View tabview = createTabView(tabHost.getContext(), title,layoutId);
		TabSpec spec = tabHost.newTabSpec(tag).setIndicator(tabview)
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
	
	@Override
	public void onBackPressed() {
	    DialogInterface.OnClickListener listener = new OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();
	        }
	    };
	    AlertDialog.Builder bldr = new Builder(this);
	    bldr.setMessage("Vuoi uscire da Bike Firenze?");
	    bldr.setPositiveButton(android.R.string.yes, listener);
	    bldr.setNegativeButton(android.R.string.no, null);
	    bldr.show();
	}
}
