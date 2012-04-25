package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.BikeRack;
import it.smartitaly.firenzeinbici.Fountain;
import it.smartitaly.firenzeinbici.GlobalState;
import it.smartitaly.firenzeinbici.OverlayManager;
import it.smartitaly.firenzeinbici.OverlayType;
import it.smartitaly.firenzeinbici.R;
import it.smartitaly.firenzeinbici.Route;
import it.smartitaly.firenzeinbici.overlays.BikeRacksOverlay;
import it.smartitaly.firenzeinbici.overlays.FountainsOverlay;
import it.smartitaly.firenzeinbici.overlays.RouteOverlay;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyRouteMapActivity extends MapActivity {

	private Route _route;
	MapView mapview;
	Button infoButton, pointsOfInterestButton;
	List<Fountain> fountains;
	List<BikeRack> racks;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapview = (MapView) findViewById(R.id.map);
		mapview.setBuiltInZoomControls(true);

		GlobalState gs = (GlobalState) getApplication();
		fountains = gs.getFountains();
		racks = gs.getBikeRacks();

		Drawable fountainMarker = getResources().getDrawable(
				R.drawable.markerfontanella);
		Drawable bikeRackMarker = getResources().getDrawable(
				R.drawable.markerrastrelliera);
		
		FountainsOverlay fountainsOverlay = new FountainsOverlay(fountainMarker,
				fountains, this);
		BikeRacksOverlay bikeRacksOverlay = new BikeRacksOverlay(bikeRackMarker, racks, this);
		
		EnumMap<OverlayType, Overlay> overlays = new EnumMap<OverlayType, Overlay>(
				OverlayType.class);
		overlays.put(OverlayType.FONTANELLE, fountainsOverlay);
		overlays.put(OverlayType.RASTRELLIERE, bikeRacksOverlay);
		
		final EnumMap<OverlayType, Boolean> overlaysStatus = gs
				.getOverlayStatus();

		final OverlayManager overlayManager = new OverlayManager(overlays,
				overlaysStatus);
		overlayManager.applyActiveOverlays(mapview);
		
		final EnumMap<OverlayType, String> overlayTypeChekboxLabels = new EnumMap<OverlayType, String>(OverlayType.class);
		overlayTypeChekboxLabels.put(OverlayType.FONTANELLE, "Fontanelle");
		overlayTypeChekboxLabels.put(OverlayType.RASTRELLIERE, "Rastrelliere");
		overlayTypeChekboxLabels.put(OverlayType.NEGOZI, "Negozi di bici");
		overlayTypeChekboxLabels.put(OverlayType.AFFITTO, "Affitto Bici");
		
		final Map<Integer, OverlayType> overlayTypeChekboxOrder = new HashMap<Integer, OverlayType>();
		overlayTypeChekboxOrder.put(0, OverlayType.RASTRELLIERE);
		overlayTypeChekboxOrder.put(1, OverlayType.FONTANELLE);
		overlayTypeChekboxOrder.put(2, OverlayType.NEGOZI);
		overlayTypeChekboxOrder.put(3, OverlayType.AFFITTO);
		
		pointsOfInterestButton = (Button) findViewById(R.id.btnpdi);
		pointsOfInterestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pointsOfInterestButton.setSelected(true);
				CharSequence[] items =
				{
					overlayTypeChekboxLabels.get(OverlayType.RASTRELLIERE),
					overlayTypeChekboxLabels.get(OverlayType.FONTANELLE),
					overlayTypeChekboxLabels.get(OverlayType.NEGOZI),
					overlayTypeChekboxLabels.get(OverlayType.AFFITTO)
				};
				
				boolean[] initialitemschecked =
				{
					overlaysStatus.get(OverlayType.RASTRELLIERE),
					overlaysStatus.get(OverlayType.FONTANELLE),
					false,
					false
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyRouteMapActivity.this);
				builder.setTitle("Punti di interesse");
				
				builder.setMultiChoiceItems(items, initialitemschecked,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which,
									boolean isChecked)
							{
								manageOverlay(which, isChecked);
							}
							
							private void manageOverlay(int which, boolean isChecked){
								if (isChecked){
									overlayManager.enable(overlayTypeChekboxOrder.get(which));
								} else {
									overlayManager.disable(overlayTypeChekboxOrder.get(which));
								}
								overlayManager.applyActiveOverlays(mapview);
							}
						});

				final AlertDialog alert = builder.create();

				alert.setButton("Close", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alert.cancel();
						pointsOfInterestButton.setSelected(false);
					}
				});
				alert.show();
			}

		});
		
		

		infoButton = (Button) findViewById(R.id.btninfo);
		infoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}

		});

		// GlobalState gs = (GlobalState)getApplication();
		_route = gs.getActiveRoute();
		RouteOverlay myOverlay = new RouteOverlay(_route, mapview);
		mapview.getOverlays().add(myOverlay);
		populateUi();
		
		
		
		myOverlay.centerMap();
	}
	
	private void populateUi(){
		populateTextView(_route.getDescription(), R.id.top_bar_description);
		populateTextView(stringify(_route.getCo2SavedInKiloGrams(), "#.# Kg"), R.id.co2_kg_label);
		populateTextView(stringify(_route.getCo2SavedInGrams(), "#.### g"), R.id.co2_g_label);
		populateTextView(Integer.toString(_route.getTravelTimeInMinutes()) + " min", R.id.travel_time_min_label);
		populateTextView(stringify(_route.getLenght(),"#.# Km"), R.id.distance_km_label);
		populateTextView(stringify(_route.getGasolineSavedInEuro(), "#.## €"), R.id.benzina_euro_label);
		populateTextView(stringify(_route.getGasolineSavedInLitre(),"#.# litri"), R.id.benzina_litri_label);
	}
	
	private String stringify(double number, String format){
		return (new DecimalFormat(format).format(number));
	}
	
	private void populateTextView(String text, int viewId){
		TextView topBarTextInfo = (TextView) findViewById(viewId);
		topBarTextInfo.setText(text);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}