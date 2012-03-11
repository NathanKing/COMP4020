package com.group2.finger_occ_demo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.DragEvent;
import android.view.MotionEvent;

/**
 * Implements view for scatter plot.
 */
public class ScatterPlotView {
	Points points;
	float sizeX;
	float sizeY;
	float marginX;
	float marginY;
	float intervalX;
	float intervalY;
	
	public ScatterPlotView(int screenWidth, int screenHeight){
		//scatter plot is within 5% of each screen side
		marginX = (float)(screenWidth * 0.1);
		marginY = (float)(screenHeight * 0.1);
		sizeX = (float) (screenWidth - (marginX * 0.5));
		sizeY = (float) (screenHeight - ((marginY * 0.5) * 6));//*6 is to account for top bar thing
		
		points = new Points((int)(sizeX), (int)(sizeY));
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {
		//shapes can obscure lines and graph background
		this.drawGraph(canvas);
		
		points.drawShapes(canvas);
	}
	
	/**
	 * Handles touching on the scatter plot. Make sure the event is sent in.
	 * Returns any alert message text.
	 */
	public String onTouch(MotionEvent event) {
		String shape = null;
		
		// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		points.checkRadius((int)event.getX(), (int)event.getY());
    	
    	if(event.getAction() == MotionEvent.ACTION_UP ){
    		// See if finger is in any of the objects
    		shape = points.inShape((int)event.getX(), (int)event.getY());
    		
    		// Make all objects normal sized
    		points.goDefaultSize();
    	}
    	
    	return shape;
	}
	
	/**
     * Detect drags on the scatterplot
     */
	public void onDrag(DragEvent event) {
		points.checkRadius((int)event.getX(), (int)event.getY());
	}
	
	/*
	 * Non generic functions
	 */
	
	/**
	 * Draws graph lines (no ticks yet).
	 */
	private void drawGraph(Canvas canvas){
		Paint black = new Paint();
		black.setStrokeWidth(3);
		
		//left line
		canvas.drawLine((float)(marginX * 0.5), (float)(marginY * 0.5), (float)(marginX * 0.5), sizeY, black);
		
		//bottom line
		canvas.drawLine((float)(marginX * 0.5), sizeY, sizeX, sizeY, black);
	}
}
