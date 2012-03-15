package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import com.group2.finger_occ_demo.data.Movie;

/**
 * Responsible for all shapes on screen. Note order of list is the way of doing
 * z-indexing.
 */
public class Points {
	
	final int RAND_RECT_NUM = 30;
	
	private int[] rect_size = {20, 20};
	private ArrayList<Square_Shape> squares;
	private ArrayList<ShapeDrawable> testSquares;
	private int radiusPX;// From the center of a shape
	private int availableWidth;
	private int availableHeight;
	
	private int[] xRange;
	private int[] yRange;
	private int xOffset;
	private int yOffset;
		
	/**
	 * Responsible for all shapes in a given box. Note order of list is the way of doing
	 * z-indexing.
	 */
	public Points(int screenWidth, int screenHeight, int[] xRange, int[] yRange){
		xOffset = 0;
		yOffset = 0;
		
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
		testSquares = new ArrayList<ShapeDrawable>();
		//init_from_data();
		init_from_data();
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
		int color = Color.GREEN;
		List<Movie> movies = canvasApp.data.getMovie();
		for (Movie movie : movies){
			x = (float) ( (availableWidth/(xRange[1] - xRange[0]) * movie.getYear1900()) + xOffset);
			y = (float) ( (availableHeight - ((availableHeight/(yRange[1] - yRange[0]) * movie.getRating())) ) + yOffset);//invert the ratings so 0 is at the bottom
			
			color = getColor(movie.getGenre().get(0));

			squares.add(new Square_Shape(movie.getTitle(), x, y, rect_size, color));
			
			testSquares.add(new ShapeDrawable(new RectShape()));
		}
	}
	
	public void filter_points(String genre, String rating){
		squares = new ArrayList<Square_Shape>();
		testSquares = new ArrayList<ShapeDrawable>();
		float x;
		float y;
		int color = Color.GREEN;
		List<Movie> movies;
		
		if(genre.equals("All") && rating.equals("All")){
			movies = canvasApp.data.getMovie();
		}
		else{
			movies = new ArrayList<Movie>();
			
			for(Movie movie : canvasApp.data.getMovie()){
				
				String g = movie.getGenre().get(0);
				int r = movie.getRating();
				
				if(genre.equals("All")){
					if(r==Integer.valueOf(rating)){
						movies.add(movie);
					}
				}
				
				else if(rating.equals("All")){
					if(g.equals(genre)){
						movies.add(movie);
					}
				}
				
				else if(g.equals(genre) && r==Integer.valueOf(rating)){
					movies.add(movie);
				}
			}
		}
		
		for (Movie movie : movies){
			x = (float) ( (availableWidth/(xRange[1] - xRange[0]) * movie.getYear1900()) + xOffset);
			y = (float) ( (availableHeight - ((availableHeight/(yRange[1] - yRange[0]) * movie.getRating())) ) + yOffset);//invert the ratings so 0 is at the bottom
			
			color = getColor(movie.getGenre().get(0));
			squares.add(new Square_Shape(movie.getTitle(), x, y, rect_size, color));
			testSquares.add(new ShapeDrawable(new RectShape()));
		}
		
	}
	
	/**
	 * Give starting coordinates for each rectangle in a random position.
	 */
	public void init_random(){
		// Omit first and last 2 squares
		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN};
		int color_num = 0;
		for (int i = 1; i < RAND_RECT_NUM; i++){
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
	 * Draw all shapes visible on the sent canvas. Due to ordering shapes in the same position end up in the
	 * same place of an array. So can use previous coordinates to determine if shape is drawable.
	 */
	public void drawShapes(Canvas on){
		Square_Shape square;
		int[] prevXY = {-1,-1};// should not be on screen, meaning first shape is always drawn
		
		for (int i = 0; i < squares.size(); i++){
			square =  squares.get(i);
			if ( !(square.getX() == prevXY[0] && square.getY() == prevXY[1])){
				square.draw(on);
				prevXY[0] = square.getX();
				prevXY[1] = square.getY();
			}
		}
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
	 * Checks an expands any shapes in the current radius. Does this by specifying and area
	 * that will need to be redrawn. The biggest objects are put in front.
	 */
	public void checkRadius(int x, int y, View view){
		Square_Shape square;
		int largeRadius = (int) (radiusPX * 1.5);
		
		//long start = System.currentTimeMillis();
		for (int i = 0; i < squares.size(); i++){
			square = squares.get(i);
			// if a square is double the distance away don't bother calculating it. This is a rough approximation keep in mind.
			if ( (square.getX() > x - largeRadius && square.getX() < x + largeRadius) &&
				 (square.getY() > y - largeRadius && square.getY() < y + largeRadius))
				square.checkRadius(x, y, radiusPX);		
		}		
		//System.out.println("Check Radius time: " + (System.currentTimeMillis() - start));
		
		reorderZ();
		
		// invalidate only this rectangle.
		view.invalidate(x - largeRadius, y - largeRadius, x + largeRadius, y + largeRadius);
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
	
	/*
	 * Getters and setters
	 */
	public void translate(int xOffset, int yOffset){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		for (Square_Shape square : squares)
			square.translate(xOffset, yOffset);
	}
	
	public void resetPosition(){
		this.xOffset = 0;
		this.yOffset = 0;
		
		for (Square_Shape square : squares)
			square.resetPosition();
	}
	
	public int getColor(String color){
		
		if(color.equals("Action")){
			return Color.WHITE;
		}
		else if(color.equals("Drama")){
			return Color.RED;	
		}
		else if(color.equals("Mystery")){
			return Color.BLUE;
		}
		else if(color.equals("Comedy")){
			return Color.MAGENTA;
		}
		else if(color.equals("Music")){
			return Color.YELLOW;
		}
		else if(color.equals("War")){
			return Color.GREEN;
		}
		else if(color.equals("Sci-Fi")){
			return Color.rgb(255, 69, 0);//orange
		}
		else if(color.equals("Western")){
			return Color.rgb(238, 221, 130);
		}
		else if(color.equals("Horror")){
			return Color.rgb(152, 251, 152);
		}
		else if(color.equals("Family")){
			return Color.rgb(160, 32, 240);
		}
		else if(color.equals("Fantasy")){
			return Color.rgb(224, 255, 255);
		}
		else if(color.equals("Romance")){
			return Color.rgb(255, 20, 147);
		}
		else if(color.equals("Short")){
			return Color.GRAY;
		}
		else if(color.equals("Thriller")){
			return Color.rgb(178, 34, 34);
		}
		return Color.WHITE;
	}
}
