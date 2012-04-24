package it.smartitaly.firenzeinbici;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class OverlayManager {

	private EnumMap<OverlayType, Overlay> _allOverlays;
	private EnumMap<OverlayType, Boolean> _overlayStatus;
	
	public OverlayManager(
			EnumMap<OverlayType, Overlay> allOverlays,
			EnumMap<OverlayType, Boolean> overlayStatus) {
		_allOverlays = allOverlays;
		_overlayStatus = overlayStatus;
	}
	
	public void enable(OverlayType type){
		_overlayStatus.put(type, new Boolean(true));
	};
	
	public void disable(OverlayType type){
		_overlayStatus.put(type, new Boolean(false));
	};
	
	public void applyActiveOverlays(MapView mapview){
		List<Overlay> overlays = mapview.getOverlays();
		overlays.removeAll(getNonActiveOverlays());
		overlays.addAll(getActiveOverlays());
		mapview.invalidate();
	}
	
	private List<Overlay> getActiveOverlays(){
		ArrayList<Overlay> activeOverlays = new ArrayList<Overlay>();
		for ( OverlayType key : _allOverlays.keySet()) {
			if (_overlayStatus.get(key).equals(new Boolean(true)))
			{
				activeOverlays.add(_allOverlays.get(key));
			}
		}
		return activeOverlays;
	}
	
	private List<Overlay> getNonActiveOverlays(){
		ArrayList<Overlay> nonActiveOverlays = new ArrayList<Overlay>();
		for ( OverlayType key : _allOverlays.keySet()) {
			if (_overlayStatus.get(key).equals(new Boolean(false)))
			{
				nonActiveOverlays.add(_allOverlays.get(key));
			}
		}
		return nonActiveOverlays;
	}
	
}
