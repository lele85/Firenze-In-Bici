package it.smartitaly.firenzeinbici;

import android.app.Application;

public class GlobalState extends Application {

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
}
