package it.smartitaly.firenzeinbici;

import java.util.EnumMap;
import java.util.List;

import android.app.Application;

public class GlobalState extends Application {
	
	private AppPaths _paths;
	private Network _network;
	private Route _activeroute;
	private List<Fountain> _fountains;
	private List<BikeRack> _bikeRacks;
	private EnumMap<OverlayType, Boolean> _overlayStatus;
	
	public void setNetwork(Network network){
		_network = network;
	}
	
	public void setOverlayStatus(EnumMap<OverlayType, Boolean> status){
		_overlayStatus = status;
	}
	public EnumMap<OverlayType, Boolean> getOverlayStatus( ){
		return _overlayStatus;
	}
	
	public Network getNetwork(){
		return _network;
	}
	
	public void setActiveRoute(Route activeroute){
		_activeroute = activeroute;
	}
	
	public Route getActiveRoute(){
		return _activeroute;
	}
	
	public void setAppPaths(AppPaths paths){
		_paths = paths;
	}
	
	public AppPaths getAppPaths(){
		return _paths;
	}
	
	public void setFountains(List<Fountain> fountains){
		_fountains = fountains;
	}
	
	public List<Fountain> getFountains(){
		return _fountains;
	}
	
	public void setBikeRacks(List<BikeRack> racks){
		_bikeRacks = racks;
	}
	
	public List<BikeRack> getBikeRacks(){
		return _bikeRacks;
	}
	
}
