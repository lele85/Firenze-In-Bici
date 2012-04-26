package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.AppPaths;
import it.smartitaly.firenzeinbici.BikeRack;
import it.smartitaly.firenzeinbici.Fountain;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.Network;
import it.smartitaly.firenzeinbici.OverlayType;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
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
				R.drawable.firenzeinbicipreview);

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

			AppPaths paths = null;
			try {
				paths = new AppPaths("FirenzeInBici", "routes.xml",
						"ciclabili-percorsi_ciclabili.kml", "rastrelliere.kml",
						"img", "fontanelli.kml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			List<Fountain> fountains = null;

			fountains = Fountain.getAll(getResources().openRawResource(
					R.raw.fontanelli));

			List<BikeRack> racks = BikeRack.getAll(getResources().openRawResource(R.raw.rastrelliere));
			Network network = new Network(getResources().openRawResource(R.raw.ciclabili_percorsi_ciclabili));

			EnumMap<OverlayType, Boolean> globalOverlayStatus = new EnumMap<OverlayType, Boolean>(
					OverlayType.class);
			globalOverlayStatus.put(OverlayType.FONTANELLE, new Boolean(false));
			globalOverlayStatus.put(OverlayType.RASTRELLIERE,
					new Boolean(false));

			final GlobalState globalState = (GlobalState) getApplication();
			globalState.setAppPaths(paths);
			globalState.setFountains(fountains);
			globalState.setBikeRacks(racks);
			globalState.setOverlayStatus(globalOverlayStatus);
			globalState.setNetwork(network);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			final Intent intent = new Intent(FirenzeInBiciActivity.this,
					CreateTabActivity.class);

			final Network network = ((GlobalState) getApplication())
					.getNetwork();

			OnNetworkDataAvailableListener listener = new OnNetworkDataAvailableListener() {

				@Override
				public void OnNetworkDataAvailable() {
					startActivity(intent);
					finish();
				}
			};

			network.setOnDataAvailableListener(listener);
			network.load();
			super.onPostExecute(result);
		}

	}
}