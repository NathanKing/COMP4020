package com.group2.finger_occ_demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnDragListener;
import android.view.WindowManager;


public class MyCanvas extends View implements OnTouchListener, OnDragListener
{	
	Shapes shapes;
	
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
        
        setup();   
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {	
		shapes.drawShapes(canvas);
	}
	
	/**
	 * When object is touched alert pops up
	 */
    public boolean onTouch(View view, MotionEvent event) {
    	// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		shapes.checkRadius((int)event.getX(), (int)event.getY());
    	
    	if(event.getAction() == MotionEvent.ACTION_UP ){
    		// See if finger is in any of the objects
    		String shape = shapes.inShape((int)event.getX(), (int)event.getY());
    		if(shape != null){
    			this.mainDialog.setMessage(shape);
    			this.mainDialog.show();
    		}
    		
    		// Make all objects normal sized
    		shapes.goDefaultSize();
    	}

		this.invalidate();
		
        return true;   
    }
    
    /**
     * Detect getting close to shapes
     */
	public boolean onDrag(View view, DragEvent event) {
		shapes.checkRadius((int)event.getX(), (int)event.getY());
		
    	this.invalidate();
    	
		return true;
	}
    
	/**
	 * Creates drawables, and alert box.
	 */
	private void setup(){
		// Get screen dimensions
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		shapes = new Shapes(screenWidth, screenHeight);
		
		//Create alert dialog with default message and OK button
		mainDialog = new AlertDialog.Builder(this.getContext()).create();
		mainDialog.setTitle("Object Selected:");
		mainDialog.setMessage("[Object]");
		mainDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {}
		});
	}
}