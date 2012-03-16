package com.group2.finger_occ_demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;



public class MyCanvas extends View implements OnTouchListener, OnDragListener
{	
	ScatterPlotView scatterView;

	Display display;
	int screenWidth;
	int screenHeight;
	
	

	AlertDialog mainDialog;
	
	public MyCanvas(Context context){
		super(context);
        setFocusable(true);
                
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        this.setOnDragListener(this);
        this.setOnGenericMotionListener(this);
        
        setup();   
	}
	
	/**
	 * Dispatches onDraw to all views.
	 */
	public void onDraw(Canvas canvas) {
		scatterView.onDraw(canvas);
	}
	
	/**
	 * Dispatches onTouch to all views. invalidate bubbles down.
	 */
    public boolean onTouch(View view, MotionEvent event) {
    	// Run through views
    	String toDisplay = scatterView.onTouch(event, view);
    	
    	// Display messages (one by one later)
    	if (toDisplay != null){
	    	this.mainDialog.setMessage(toDisplay);
			this.mainDialog.show();
    	}

    	// Feed the state machine
    	States.massageEvent(event);
    	
        return true;   
    }
    
    /**
     * Sends onDrag to each view.
     */
	public boolean onDrag(View view, DragEvent event) {
		scatterView.onDrag(event, view);
		
    	// Feed state machine
    	States.massageEvent(event);
    	
		return true;
	}
	
	public boolean onGenericMotionListener(View v, MotionEvent event) {
		States.massageEvent(event);
		return true;
	}
    
	/**
	 * Creates views, and alert box.
	 */
	private void setup(){
		// Get screen dimensions
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		// Create each view
		scatterView = new ScatterPlotView(screenWidth, screenHeight);
		
		//Create alert dialog with default message and OK button
		mainDialog = new AlertDialog.Builder(this.getContext()).create();
		mainDialog.setTitle("Object Selected:");
		mainDialog.setMessage("[Object]");
		mainDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {}
		});
	}
}