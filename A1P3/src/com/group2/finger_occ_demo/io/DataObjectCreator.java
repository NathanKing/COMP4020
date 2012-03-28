package com.group2.finger_occ_demo.io;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.group2.finger_occ_demo.Users;
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
	 * Creates all movie objects from the specified file and returns them.
	 */
	public DataObjects createMovieObjects(String fileName){
		return (DataObjects) createObjects(fileName, DataObjects.class);
	}
	
	/**
	 * Creates all user objects from the specified file and returns them.
	 */
	public Users createUserObjects(String fileName){
		return (Users) createObjects(fileName, Users.class);
	}
	
	/**
	 * Creates all objects from the specified file and returns them.
	 */
	public Object createObjects(String fileName, Class c){
		ReadFile file;
		ObjectMapper mapper = new ObjectMapper();//For Jackson JSON
		mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);//handles getter/setter with no corresponding variable
		
		// Read in Files
        file = new ReadFile(assets, context);

        try {
        	String contents = file.get(fileName);
        	if (contents == null)
        		return null;
			return mapper.readValue(contents, c);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return null;
	}
}
