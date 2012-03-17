package com.group2.finger_occ_demo;

import com.group2.finger_occ_demo.data.Movie;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Implements view for scatter plot.
 */
public class ScatterPlotView {
	public Points points;
	private float sizeX;
	private float sizeY;
	private float marginX;
	private float marginY;
	
	private int[] xRange = {0, 100};// currently year produced
	private int[] yRange = {0, 10};// currently rating
	
	private int xOffset;
	private int yOffset;
	
	public ScatterPlotView(int screenWidth, int screenHeight){
		//scatter plot is within 5% of each screen side
		marginX = (float)(screenWidth * 0.1);
		marginY = (float)(screenHeight * 0.1);
		sizeX = (float) (screenWidth - (marginX * 2));
		sizeY = (float) (screenHeight - ((marginY * 0.5) * 7));//*7 is to account for top bar thing
		
		xOffset = 0;
		yOffset = 0;
		
		points = new Points((int)(sizeX), (int)(sizeY), xRange, yRange);
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {
		//shapes can obscure lines and graph background, so this is why graph is first.
		this.drawGraph(canvas);
		points.drawShapes(canvas);
	}
	
	/**
	 * Handles touching on the scatter plot. Make sure the event is sent in.
	 * Returns any alert message text.
	 * @param view 
	 */
	public Movie onTouch(MotionEvent event, View view) {
		Movie shape = null;
		
		// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		points.checkRadius((int)event.getX(), (int)event.getY(), view);
    	
    	if(event.getAction() == MotionEvent.ACTION_UP ){
    		// See if finger is in any of the objects
    		shape = points.inShape((int)event.getX(), (int)event.getY());
    		
    		// Make all objects normal sized
    		points.goDefaultSize();
    		
    		view.invalidate();
    	}
    	
    	return shape;
	}
	
	/**
     * Detect drags on the scatterplot
	 * @param view 
     */
	public void onDrag(DragEvent event, View view) {
		points.checkRadius((int)event.getX(), (int)event.getY(), view);
	}
	
	/*
	 * Graph translation and zooming
	 */
	
	/**
	 * Offsets the CURRENT scatter plot by the given amount.
	 */
	public void translatePlot(int xOffset, int yOffset){
		this.xOffset += xOffset;
		this.yOffset += yOffset;
		
		points.translate(xOffset, yOffset);
	}
	
	/**
	 * Resets scatter plot. Currently only resets panning.
	 */
	public void resetGraph(){
		this.xOffset = 0;
		this.yOffset = 0;
		
		points.resetPosition();
	}
	
	/*
	 * Non generic draw functions
	 */
	
	/**
	 * Draws graph lines (no ticks yet).
	 */
	private void drawGraph(Canvas canvas){
		Paint black = new Paint();
		black.setStrokeWidth(3);
		
		//left line
		canvas.drawLine((float)(marginX * 0.5) + xOffset, (float)(marginY * 0.5) + yOffset, (float)(marginX * 0.5) + xOffset, sizeY + yOffset, black);
		
		//bottom line
		canvas.drawLine((float)(marginX * 0.5) + xOffset, sizeY + yOffset, sizeX + xOffset, sizeY + yOffset, black);
	}
}
