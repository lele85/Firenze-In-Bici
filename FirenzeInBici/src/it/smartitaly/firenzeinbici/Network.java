package it.smartitaly.firenzeinbici;

import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.util.ArrayList;


public class Network {

	private ArrayList<Route> _routes = new ArrayList<Route>();
	private OnNetworkDataAvailableListener _listener;
	
	public Network(){
		
	}
	
	public void onRoutesParsed(ArrayList<Route> routes){
		_routes = routes;
		_listener.OnNetworkDataAvailable();
	}
	
	public void setOnDataAvailableListener(OnNetworkDataAvailableListener listener){
		_listener = listener;
	}
	
	public void load(){
		ParseAllNetwork task = new ParseAllNetwork(this);
		task.execute("");
	}
	
	public ArrayList<Route> getRoutes(){
		return _routes;
	}
}
