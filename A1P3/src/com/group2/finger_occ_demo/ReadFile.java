package com.group2.finger_occ_demo;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * Reads in the specified JSON file, needs the AssetManager of the current
 * context (likely this in main class) to read files from local storage. 
 */

public class ReadFile {
	
	private AssetManager assets;
	
	public ReadFile(AssetManager assets){
		this.assets = assets;
	}
	
	/**
	 * Returns a string of the file read.
	 */
	public String get(String fileName){
		InputStream input_strm;
		
        try{
        	// get the file and return it
        	input_strm = assets.open(fileName);
        	return IOUtils.toString(input_strm, "UTF-8");
        }
        catch (Exception e){
            Log.e("FileRead", e.toString());
        }
        
        return "Error: error should be caught before here";
	}
}
