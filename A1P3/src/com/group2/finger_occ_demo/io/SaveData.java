package com.group2.finger_occ_demo.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.group2.finger_occ_demo.Users;
import com.group2.finger_occ_demo.data.DataObjects;

import android.content.Context;

public class SaveData {
	private Context context;
	
	public SaveData(Context context){
		this.context = context;
	}
	
	/**
	 * Save the sent in movie and user data.
	 */
	public void save(String movieName, DataObjects movieData, String usersName, Users userData){
		saveFile(movieName, movieData);
		System.out.println("Got here");
		saveFile(usersName, userData);
	}
	
	private void saveFile(String name, Object data){
		ObjectMapper mapper = new ObjectMapper();//For Jackson JSON
	    FileOutputStream testFile = null;
	    
		try {
			testFile = context.openFileOutput(name, Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    
	    try {
			mapper.writeValue(testFile, data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
