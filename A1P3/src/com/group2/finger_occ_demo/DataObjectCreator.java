package com.group2.finger_occ_demo;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.group2.finger_occ_demo.data.DataObjects;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Gets a read in json file and converts it to equivalent Java objects.
 */
public class DataObjectCreator {
	
	private AssetManager assets;
	private Context context;
	
	public DataObjectCreator(AssetManager assets, Context context){
		this.assets = assets;
		this.context = context;
	}
	
	/**
	 * Creates all objects from the specified file and returns them.
	 */
	public DataObjects createObjects(String fileName){
        return fromJackson(fileName);
	}
	
	public DataObjects fromJackson(String fileName){
		ReadFile file;
		ObjectMapper mapper = new ObjectMapper();//For Jackson JSON
		mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);//handles getter/setter with no corresponding variable
		
		// Read in Files
        file = new ReadFile(assets, context);

        try {
			return mapper.readValue(file.get(fileName), DataObjects.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return null;
	}
	
	/*
	public DataObjects fromGson(String fileName){
		ReadFile file;
		String fileContents;
		Gson gson = new Gson();
		
		// Read in Files
        file = new ReadFile(assets, context);
        fileContents = file.get(fileName);

        return gson.fromJson(fileContents, DataObjects.class);
	}*/
}
