package com.group2.a1;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.Activity;
import android.os.Bundle;

public class canvasApp extends Activity {
	
	MyCanvas canvasView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		canvasView = new MyCanvas(this);		
        setContentView(canvasView);
        canvasView.requestFocus();
    }
    
    @Override
    public void onBackPressed(){
    	
    	this.finish();
    }
    
}