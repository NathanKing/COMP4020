package com.group2.finger_occ_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.group2.finger_occ_demo.data.Movie;



public class MyCanvas extends View implements OnTouchListener, OnDragListener
{	
	public static Movie movieFound;//no time so must use a movie found class var
	ScatterPlotView scatterView;
	Display display;
	int screenWidth;
	int screenHeight;
	canvasApp context;

	AlertDialog mainDialog;
	public MyCanvas(canvasApp context){
		super(context);
        setFocusable(true);
        
        this.context = context;
                
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        this.setOnDragListener(this);
        
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
    	Movie foundMovie = scatterView.onTouch(event, view);
    	
    	// Open the Movie View Display
    	if (foundMovie != null){
    		context.startActivity(new Intent(context, MovieActivity.class));
    		// TODO Change later
    		movieFound = foundMovie;
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