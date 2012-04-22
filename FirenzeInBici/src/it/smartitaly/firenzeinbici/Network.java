package it.smartitaly.firenzeinbici;

import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.io.File;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;


public class Network {

	private ArrayList<ArrayList<GeoPoint>> _paths = new ArrayList<ArrayList<GeoPoint>>();
	private OnNetworkDataAvailableListener _listener;
	private File _definitionFile;
	
	public Network(File definitionFile){
		_definitionFile =  definitionFile;
	}
	
	public void onRoutesParsed(ArrayList<ArrayList<GeoPoint>> paths){
		_paths = paths;
		_listener.OnNetworkDataAvailable();
	}
	
	public void setOnDataAvailableListener(OnNetworkDataAvailableListener listener){
		_listener = listener;
	}
	
	public void load(){
		ParseAllNetwork task = new ParseAllNetwork(this, _definitionFile);
		task.execute("");
	}
	
	public ArrayList<ArrayList<GeoPoint>> getPaths(){
		return _paths;
	}
}
