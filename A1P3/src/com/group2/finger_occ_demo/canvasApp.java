package com.group2.finger_occ_demo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.group2.finger_occ_demo.data.DataObjects;

/**
 * Initializes Canvas and starts application.
 */
public class canvasApp extends Activity {
	
	public static DataObjects data;
	
	//Application Constants
	private final String FILE_NAME = "movies.json";
	
	private MyCanvas canvasView;
	
    /** Called when the activity is first created.. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DataObjectCreator creator = new DataObjectCreator(this.getAssets(), getBaseContext());
    	
        super.onCreate(savedInstanceState);
        
        // Give default view until main screen is loaded
        setContentView(R.layout.main);
        
        // Get the data from the JSON file
        long start = System.currentTimeMillis();
        data = creator.createObjects(FILE_NAME);
        System.out.println("Time taken to load is: " + (System.currentTimeMillis() - start));
        
        // Create a new canvas set it as the view and give it focus. Also give it
        // the data created from the json file.
        
		canvasView = new MyCanvas(this);
        setContentView(canvasView);
        canvasView.setBackgroundColor(Color.WHITE);
        canvasView.requestFocus();
    }
    
    @Override
    public void onBackPressed(){
    	ObjectMapper mapper = new ObjectMapper();//For Jackson JSON
        FileOutputStream testFile = null;
        
		try {
			testFile = getBaseContext().openFileOutput(FILE_NAME, Context.MODE_WORLD_READABLE);
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
    	
    	this.finish();
    }
    
}