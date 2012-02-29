package com.group2.finger_occ_demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class canvasApp extends Activity {
	
	MyCanvas canvasView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Give default view until main screen is loaded
        setContentView(R.layout.main);
        
        // Create a new canvas set it as the view and give it focus
		canvasView = new MyCanvas(this);		
        setContentView(canvasView);
        canvasView.setBackgroundColor(Color.WHITE);
        canvasView.requestFocus();
    }
    
    @Override
    public void onBackPressed(){
    	
    	this.finish();
    }
    
}