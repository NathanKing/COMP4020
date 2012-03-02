package com.group2.finger_occ_demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

/**
 * Initializes Canvas and starts application.
 */
public class canvasApp extends Activity {
	
	//Application Constants
	private final String FILE_NAME = "movies.json";
	
	private MyCanvas canvasView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DataObjectCreator creator = new DataObjectCreator(this.getAssets());
    	
        super.onCreate(savedInstanceState);
        
        // Give default view until main screen is loaded
        setContentView(R.layout.main);
        
        // Create a new canvas set it as the view and give it focus. Also give it
        // the data created from the json file.
		canvasView = new MyCanvas(this, creator.createObjects(FILE_NAME));		
        setContentView(canvasView);
        canvasView.setBackgroundColor(Color.WHITE);
        canvasView.requestFocus();
    }
    
    @Override
    public void onBackPressed(){
    	this.finish();
    }
    
}