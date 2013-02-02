package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.BikeRack;
import it.smartitaly.firenzeinbici.Fountain;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.OverlayType;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.RechargeSpot;
import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.util.EnumMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class FirenzeInBiciActivity extends Activity {

	ParseAll parse;
	ImageView imagepreview;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagepreview);

		Drawable firenzeinbicipreview = getResources().getDrawable(
				R.drawable.splash);
		
		imagepreview = (ImageView) findViewById(R.id.imagepreview);
		imagepreview.setBackgroundDrawable(firenzeinbicipreview);

		parse = new ParseAll();
		parse.execute();

	}

	@Override
	public void onBackPressed() {
		parse.cancel(true);
		finish();
	}

	public class ParseAll extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {

			List<Fountain> fountains = Fountain
					.getAll(
							getResources()
							.openRawResource(R.raw.fontanelli));

			List<BikeRack> racks = BikeRack
					.getAll(
							getResources()
							.openRawResource(R.raw.rastrelliere));
			
			List<RechargeSpot> rechargeSpots = RechargeSpot
					.getAll(
							getResources()
							.openRawResource(R.raw.arredo_urbano_colonninericarica));
			
			Network network = new Network(getResources().openRawResource(R.raw.ciclabili_percorsi_ciclabili));

			EnumMap<OverlayType, Boolean> globalOverlayStatus = new EnumMap<OverlayType, Boolean>(
					OverlayType.class);
			globalOverlayStatus.put(OverlayType.FONTANELLE, new Boolean(false));
			globalOverlayStatus.put(OverlayType.RASTRELLIERE, new Boolean(false));
			globalOverlayStatus.put(OverlayType.COLONNINE_RICARICA, new Boolean(false));

			final GlobalState globalState = (GlobalState) getApplication();
			globalState.setFountains(fountains);
			globalState.setBikeRacks(racks);
			globalState.setRechargeSpots(rechargeSpots);
			globalState.setOverlayStatus(globalOverlayStatus);
			globalState.setNetwork(network);

			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			final Intent intent = new Intent(FirenzeInBiciActivity.this,
					MainTabActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

			final Network network = ((GlobalState) getApplication())
					.getNetwork();
			
			OnNetworkDataAvailableListener listener = new OnNetworkDataAvailableListener() {
				
				@Override
				public void OnNetworkDataAvailable() {
					startActivity(intent);
					imagepreview = null;
				}
			};

			network.setOnDataAvailableListener(listener);
			network.load();
			super.onPostExecute(result);
		}
	}
}