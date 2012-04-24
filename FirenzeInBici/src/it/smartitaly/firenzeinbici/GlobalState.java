package it.smartitaly.firenzeinbici;

import java.util.List;

import android.app.Application;

public class GlobalState extends Application {
	
	private AppPaths _paths;
	private Network _network;
	private Route _activeroute;
	private List<Fountain> _fountains;
	private OverlayManager _overlayManager;
	
	public void setNetwork(Network network){
		_network = network;
	}
	
	public void setOverlayManager(OverlayManager manager){
		_overlayManager = manager;
	}
	public OverlayManager getOverlayManager( ){
		return _overlayManager;
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
	
}
