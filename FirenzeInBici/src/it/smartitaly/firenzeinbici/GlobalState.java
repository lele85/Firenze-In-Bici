package it.smartitaly.firenzeinbici;

import android.app.Application;
import android.graphics.Path;
import android.graphics.drawable.shapes.PathShape;

public class GlobalState extends Application {
	
	private AppPaths _paths;
	private Network _network;
	private Route _activeroute;
	
	public void setNetwork(Network network){
		_network = network;
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
}
