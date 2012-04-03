package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.List;
import com.group2.finger_occ_demo.data.Movie;

import android.content.Context;
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
	
	private int[] xRange = {0, 110};// currently year produced
	private int[] yRange = {0, 10};// currently rating
	
	private int xOffset;
	private int yOffset;
	
	final private int TICK_LINES_Y = 10;
	final private int TICK_LINES_X = 11;
	final private int TICK_SIZE = 15;//in px
	
	final private int POINT_OFFSET = 0;
	
	private canvasApp context;
	
	public ScatterPlotView(int screenWidth, int screenHeight, canvasApp context){
		this.context = context;
		
		//scatter plot is within 5% of each screen side
		marginX = (float)(screenWidth * 0.1);
		marginY = (float)(screenHeight * 0.1);
		sizeX = (float) (screenWidth - (marginX * 2));
		sizeY = (float) (screenHeight - ((marginY * 0.5) * 7));//*7 is to account for bottom bar
		
		xOffset = 0;
		yOffset = 0;
		
		points = new Points((int)(marginX * 0.5), (int)(marginY * 0.5) - POINT_OFFSET, sizeX, sizeY, xRange, yRange);
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {
		//shapes can obscure lines and graph background, so this is why graph is first.
		points.drawGraph(canvas);
		points.drawShapes(canvas);
	}
	
	/**
	 * Handles touching on the scatter plot. Make sure the event is sent in.
	 * Returns any alert message text.
	 */
	public ArrayList<Movie> onTouch(MotionEvent event, View view) {
		ArrayList<Movie> movies = null;
		
		// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		points.checkRadius((int)event.getX(), (int)event.getY(), view);
		    	
    	if(event.getAction() == MotionEvent.ACTION_UP ){
    		// See if finger is in any of the objects
    		movies = points.inShape((int)event.getX(), (int)event.getY());
    			
    		view.invalidate();
    	}
    	    	
    	return movies;
	}
	
	/**
     * Detect drags on the scatterplot.
     */
	public void onDrag(DragEvent event, View view) {
		points.checkRadius((int)event.getX(), (int)event.getY(), view);
	}
	
	/*
	 * Graph translation and zooming
	 */
	
	/**
	 * Resets scatter plot. Currently only resets panning.
	 */
	public void resetGraph(){
		this.xOffset = 0;
		this.yOffset = 0;
		
		points.resetPosition();
	}
	
	/**
	 * Resets and restores all points in the scatterplot
	 */
	public void resetPoints(List<Movie> movies){
		points.init_from_data(movies);
	}
}
