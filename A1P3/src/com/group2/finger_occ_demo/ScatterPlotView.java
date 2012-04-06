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
	
	private float zoom = 1.0f;

	final private int POINT_OFFSET = 0;
	
	public ScatterPlotView(int screenWidth, int screenHeight, canvasApp context){
		
		//scatter plot is within 5% of each screen side
		marginX = (float)(screenWidth * 0.1);
		marginY = (float)(screenHeight * 0.1);
		sizeX = (float) (screenWidth - (marginX * 2));
		sizeY = (float) (screenHeight - ((marginY * 0.5) * 7));//*7 is to account for bottom bar
		
		points = new Points((int)(marginX * 0.5), (int)(marginY * 0.5) - POINT_OFFSET, sizeX, sizeY, xRange, yRange);
	}
	
	/**
	 * Whenever the canvas is redrawn, these objects are drawn.
	 */
	public void onDraw(Canvas canvas) {
		points.drawShapes(canvas, zoom);
		points.drawGraph(canvas, zoom);
	}
	
	/**
	 * Handles touching on the scatter plot. Make sure the event is sent in.
	 * Returns any alert message text.
	 */
	private Point2D oldPos = new Point2D(0, 0);
	public void onTouch(MotionEvent event, View view) {
		// check if finger is in radius to resize any objects (want to make objects bigger on touch and drag)
		float x = event.getX();
		float y = event.getY();
		
		// Remove jitter, save battery
		if (oldPos.x != x && oldPos.y != y)
		{
			points.checkRadius((int)x, (int)y, view);
			oldPos.x = x;
			oldPos.y = y;
		}
	}
	
	public ArrayList<Movie> getMovies(MotionEvent event)
	{
		return points.inShape((int)event.getX(), (int)event.getY());		
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
		points.resetPosition();
	}
	
	public void invalidate()
	{
		points.invalidate();
	}
	
	/**
	 * Resets and restores all points in the scatterplot
	 */
	public void resetPoints(List<Movie> movies){
		points.init_from_data(movies);
	}
	
	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}
	
	public void setOffset(float x, float y)
	{
		SquareShape.setOffset(x, y);
		points.setOffset(x, y);
	}
}
