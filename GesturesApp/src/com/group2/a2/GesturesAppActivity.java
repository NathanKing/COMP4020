package com.group2.a2;


import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GesturesAppActivity extends Activity implements OnGesturePerformedListener{
	GestureLibrary mLibrary;
	MyView view;	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
       
        view = new MyView(this);
       
        gestures.addView(view);
    	
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.shapes4);
        if (!mLibrary.load()) {
        	finish();
        }
        gestures.addOnGesturePerformedListener(this);
    }
	
	public void colorClicked(View v){
		
		RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup);
		
		int check = rg.getCheckedRadioButtonId();
		
		String radioButtonSelected = "";
		 
		switch (check) {
		  case R.id.redButton : view.paint = view.red;
		                   	          break;
		  case R.id.blueButton : view.paint = view.blue;
				                      break;
		  case R.id.greenButton : view.paint = view.green;
				                      break;
		}
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		
		Shape shape;
		
		RectF r = gesture.getBoundingBox();
		
		float[]points = gesture.getStrokes().get(0).points;
	
		
				
		Log.w("loc center", String.valueOf(r.centerX()));
	
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
		
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
				
				if(prediction.name.equals("rect")){
					
					shape = new Rectangle(r, points);
					view.shapes.add(shape);
					
					Log.w("prediction", "rect");
				}
				else if(prediction.name.equals("tri")){
					
					shape = new Triangle(r, points);
					view.shapes.add(shape);

					Log.w("prediction", "tri");
				}
				else if(prediction.name.equals("line")){
					
					shape = new Line(r, points);
					view.shapes.add(shape);
					
					Log.w("prediction", "line");
				}
				
				else if(prediction.name.equals("circle")){
					
					shape = new Circle(r, points);
					view.shapes.add(shape);
					
					Log.w("prediction", "circle");
				}
				else if(prediction.name.equals("delete")){
					
					view.deleteShape(r, points);
					//shape = new Delete(r, points);
					//view.shapes.add(shape);
					
					Log.w("prediction", "delete");
				}
			}
		}
		
	}
}