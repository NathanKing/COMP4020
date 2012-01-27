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
        
        try {
			canvasView = new MyCanvas(this);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setContentView(canvasView);
        canvasView.requestFocus();
    }
    
    @Override
    public void onBackPressed(){
    	
    	this.finish();
    }
    
}