package it.smartitaly.firenzeinbici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.Map;

import android.os.Environment;

public class AppPaths {
	
	public static enum Resources {
		ROUTES_FILE,
		ALL_NETWORK_FILE,
		RACKS_FILE,
		IMAGES_DIR
	};
	
	private File _storageBasePath;

	
	private Map<Resources,File> _files;
	
	public AppPaths(
			String storageFolderName,
			String routesFileName,
			String allNetworkFileName,
			String racksFileName,
			String imagesDirName
			) throws FileNotFoundException {
		_files = new EnumMap<Resources, File>(Resources.class);
		_storageBasePath = new File(Environment.getExternalStorageDirectory(), storageFolderName);
		_files.put(Resources.ALL_NETWORK_FILE, new File(_storageBasePath, allNetworkFileName));
		_files.put(Resources.RACKS_FILE, new File(_storageBasePath, racksFileName));
		_files.put(Resources.ROUTES_FILE, new File(_storageBasePath, routesFileName));
		_files.put(Resources.IMAGES_DIR, new File(_storageBasePath, imagesDirName));
		for (Resources resource : _files.keySet()) {
			verifyExists(resource);
		}
	}
	
	public File getFile(Resources resource){
		return _files.get(resource);
	}
	
	private void verifyExists (Resources resource) throws FileNotFoundException{
		String filePath =  _files.get(resource).toString();
		if (!_files.get(resource).exists()){
			throw new FileNotFoundException("File non trovato: " + filePath);
		}
	}
	
	public File getImageFile(String fileName){
		File file = new File(getFile(Resources.IMAGES_DIR),fileName);
		return file;
	}
}
