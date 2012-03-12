package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.group2.finger_occ_demo.data.Movie;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Responsible for all shapes on screen. Note order of list is the way of doing
 * z-indexing.
 */
public class Points {
	
	final int RECT_NUM = 30;
	
	private int[] rect_size = {20, 20};
	private ArrayList<Square_Shape> squares;
	private int radiusPX;// From the center of a shape
	private int availableWidth;
	private int availableHeight;
	
	private int[] xRange;
	private int[] yRange;
	
	/**
	 * Responsible for all shapes on screen. Note order of list is the way of doing
	 * z-indexing.
	 */
	public Points(int screenWidth, int screenHeight, int[] xRange, int[] yRange){
		// Get the available width and ranges
		this.availableWidth = screenWidth;
		this.availableHeight = screenHeight;
		this.xRange = xRange;
		this.yRange = yRange;
		
		// Derive rectangle size from screen size so it looks nice (only for now)
		rect_size[0] = (int) (this.availableWidth * 0.02);
		rect_size[1] = (int) (this.availableWidth * 0.02);
		
		// Also derive radius from screen size, make it 4 times the shapes size
		radiusPX = rect_size[0] * 4;
		
		squares = new ArrayList<Square_Shape>();
		init_from_data();
		//init_random();
	}
	
	/**
	 * Currently uses year on x and rating on height. Formula is:
	 * (n/m) * r = x or y position,
	 * where n is the maximum graph value for the axis, n is the maximum value
	 * for the data points i.e. for rating 10, and r is the current value of the point.
	 */
	public void init_from_data(){
		float x;
		float y;
		
		List<Movie> movies = canvasApp.data.getMovie();
		for (Movie movie : movies){
			x = (float) ((availableWidth/xRange[1]) * movie.getYear1900());
			y = (float) (availableHeight - ((availableHeight/yRange[1]) * movie.getRating()));//invert the ratings so 0 is at the bottom
			//System.out.println(x + ":" + y + ":" + movie.getYear1900());
			
			squares.add(new Square_Shape(movie.getTitle() + ":" + movie.getRating() + ":" + movie.getYear(), x, y, rect_size, Color.GREEN));
		}
		System.out.println(movies.size());
	}
	
	/**
	 * Give starting coordinates for each rectangle in a random position.
	 */
	public void init_random(){
		// Omit first and last 2 squares
		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN};
		int color_num = 0;
		for (int i = 1; i < RECT_NUM; i++){
			squares.add(new Square_Shape(i + "", getR(availableWidth), getR(availableHeight), rect_size, colors[color_num]));
			
			color_num++;
			if (color_num == 3)
				color_num = 0;
		}
	}
	// gets a dimension randomly
	private int getR(int screenDimen){
		return (int) ((Math.round(Math.random() * 100) * 0.01) * screenDimen);
	}
	
	/**
	 * Draw all shapes on the sent canvas
	 */
	public void drawShapes(Canvas on){
		for (int i = 0; i < squares.size(); i++)
			squares.get(i).draw(on);
	}
	
	/**
	 * If in any shape on the canvas.
	 */
	public String inShape(int x, int y){
		int[] position = {x, y};
		
		// See if in any of the shapes, backwards loop because current biggest
		// objects are in the end of the array list
		for (int i = squares.size() - 1; i > 0; i--)
			if (squares.get(i).inShape(position) == true)
				return squares.get(i).name;
		
		return null;
	}
	
	/**
	 * Checks an expands any shapes in the current radius. The biggest objects
	 * are put in front.
	 */
	public void checkRadius(int x, int y){
		for (int i = 0; i < squares.size(); i++)
			squares.get(i).checkRadius(x, y, radiusPX);
		
		reorderZ();
	}
	
	/**
	 * Reorders z-indexing of shapes (list order) by the current size of
	 * the shape. Greatest elements are last as they are rendered last
	 * overlapping other elements.
	 */
	private void reorderZ(){
		Collections.sort(squares, new Comparator<Object>(){
			public int compare(Object obj1, Object obj2) {
				Square_Shape sqr1 = (Square_Shape)obj1;
				Square_Shape sqr2 = (Square_Shape)obj2;
				
				return sqr1.getSize() - sqr2.getSize();
			}
        });
	}
	
	/**
	 * Sets all shapes to the default size
	 */
	public void goDefaultSize(){
		for (int i = 0; i < squares.size(); i++)
			squares.get(i).setDefaultSize();
	}
}
