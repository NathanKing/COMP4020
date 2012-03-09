package com.group2.finger_occ_demo;

import com.google.gson.Gson;
import com.group2.finger_occ_demo.data.DataObjects;

import android.content.res.AssetManager;

/**
 * Gets a read in json file and converts it to equivalent Java objects.
 */
public class DataObjectCreator {
	
	private AssetManager assets;
	
	public DataObjectCreator(AssetManager assets){
		this.assets = assets;
	}
	
	/**
	 * Creates all objects from the specified file and returns them.
	 */
	public DataObjects createObjects(String fileName){
		ReadFile file;
		String fileContents;
		Gson gson = new Gson();
		
		// Read in Files
        file = new ReadFile(assets);
        fileContents = file.get(fileName);

        // From string of contents create Java Objects
        return gson.fromJson(fileContents, DataObjects.class);
	}
}
