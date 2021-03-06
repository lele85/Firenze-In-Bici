package it.smartitaly.firenzeinbici;

import it.smartitaly.firenzeinbici.listeners.OnNetworkDataAvailableListener;

import java.io.InputStream;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;


public class Network {

	private ArrayList<ArrayList<GeoPoint>> _paths = new ArrayList<ArrayList<GeoPoint>>();
	private OnNetworkDataAvailableListener _listener;
	private InputStream _definitionFile;
	
	public Network(InputStream definitionFile){
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
		ParseAllNetworkXmlPull task = new ParseAllNetworkXmlPull(this, _definitionFile);
		task.execute("");
	}
	
	public ArrayList<ArrayList<GeoPoint>> getPaths(){
		return _paths;
	}
}
