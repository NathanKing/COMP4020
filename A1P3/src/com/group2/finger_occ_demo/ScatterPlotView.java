package com.group2.finger_occ_demo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.DragEvent;
import android.view.MotionEvent;

/**
 * Implements view for scatter plot.
 */
public class ScatterPlotView {
	private Points points;
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
		sizeX = (float) (screenWidth - (marginX * 0.5));
		sizeY = (float) (screenHeight - ((marginY * 0.5) * 7));//*7 is to account for top bar thing
		
		xOffset = 0;
		yOffset = 0;
		
		points = new Points((int)(sizeX), (int)(sizeY), xRange, yRange);
		
		//positionGraph();
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {
		//shapes can obscure lines and graph background
		this.drawGraph(canvas);
		
		long start = System.currentTimeMillis();
		points.drawShapes(canvas);
		System.out.println("Time taken to draw is: " + (System.currentTimeMillis() - start));
	}
	
	/**
	 * Handles touching on the scatter plot. Make sure the event is sent in.
	 * Returns any alert message text.
	 */
	public String onTouch(MotionEvent event) {
		//TODO remove once done testing
		translatePlot(50, 20);
		
		String shape = null;
		
		// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		long start = System.currentTimeMillis();
		points.checkRadius((int)event.getX(), (int)event.getY());
		System.out.println("Time taken to check is: " + (System.currentTimeMillis() - start));
    	
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
