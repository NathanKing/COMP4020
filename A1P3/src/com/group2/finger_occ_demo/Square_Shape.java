package com.group2.finger_occ_demo;

import com.group2.finger_occ_demo.data.Movie;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Tracks square size, location and color. Resizes square when in radius by in how much
 * in radius * 0.5 * 1 * current size.
 */
public class Square_Shape {
	public Movie movie;
	private int xDefault;
	private int yDefault;
	private int x;
	private int y;
	private int[] size;
	Paint color;
	Paint borderColor;
	private double resizeBy;//Computed factor to scale object by
	
	private boolean stale;				// Do we need to update these rects when we draw?
	private Rect shape = new Rect();
	private Rect border = new Rect();
	
	// constants
	final private int BORDER = 2;//in pixels
	final private double resizeF = 0.05;//Constant to resize by {2 for device}
	
	static final int OFFSET_Y = 10;	
	
	// levels to display progressive info.
	final private double TEXT_APPEAR = 1.3;//times
	
	public Square_Shape(Movie movie, float x, float y, int[] size, int colorNum) {
		this.movie  = movie;
		
		this.x = (int) x;
		this.y = (int) y;
		this.size = size;
		this.resizeBy = 0;
		
		this.stale = true;
		
		color = new Paint();
		color.setColor(colorNum);
		color.setAntiAlias(true);
		
		// black border
		borderColor = new Paint(Color.BLACK);
	}
	
	/**
	 * Draw rectangle on current canvas with text denoting its number, resize from center if necessary. Contains
	 * a black border.
	 */
	public void draw(Canvas on){
		if (stale)
		{
			int expandByX = (int) (size[0] * resizeBy);
			int expandByY = (int) (size[1] * resizeBy);
			
			// Offset slightly above finger
			int offsetY  = (int) (-10 * resizeBy);
			
			shape.top    = y - expandByY + offsetY;
			shape.bottom = y + size[1] + expandByY + offsetY;
			shape.left   = x - expandByX;
			shape.right  = x + size[0] + expandByX;
			
			border.set(shape);
			border.top	+= BORDER;
			border.bottom -= BORDER;
			border.left	+= BORDER;
			border.right  -= BORDER;
			
			stale = false;
		}
		
		on.drawRect(shape, borderColor);
		on.drawRect(border, color);
		
		// Decide on when to display extra data
		if (resizeBy > TEXT_APPEAR)
			on.drawText(movie.getTitle(), border.left + 3, border.top + 10, borderColor);
	}
	
	/**
	 * checks if the given coordinates are in the current shape.
	 */
	public boolean inShape(int[] position){
		int expandByX = (int) (size[0] * resizeBy);
		int expandByY = (int) (size[1] * resizeBy);
		return ((position[0] > getX() - expandByX) && (position[0] < getX() + size[0] + expandByX && (position[1] > y - expandByY) && (position[1] < y + size[1] + expandByY) ));
	}
	
	/**
	 * Checks the current square is in the circle with the specified center and radius. Does this
	 * by comparing vector between circle and each of the squares 4 points.
	 * @return Whether the finger was in the current shapes radius
	 */
	public boolean checkRadius(int circle_x, int circle_y, int radius){
		// Center of square
		int x = this.shape.centerX();
		int y = this.shape.centerY();

		int f = Math.abs(circle_x - x);
		int e = Math.abs(circle_y - y);
		double vector = Math.sqrt(Math.pow(f, 2) + Math.pow(e, 2));
		
		if (radius - vector > 0)
		{
			resizeBy = (radius - vector) * resizeF;
			stale = true;
		}
		else
			setDefaultSize();
	
		return false;
	}
	
	/**
	 * Set resizeBy to 1 making the shapes size normal.
	 */
	public void setDefaultSize(){
		stale = true;
		resizeBy = 0;
	}
	
	/**
	 * Reset square to the position it originally displayed in.
	 */
	public void resetPosition() {
		this.setX(xDefault);
		this.setY(yDefault);
	}
	
	/*
	 * Getters and Setters
	 */
	
	/**
	 * Receives computed size.
	 */
	public int getSize(){
		return (int) (size[0] * (resizeBy));
	}
	
	public double resizeValue(){
		return resizeBy;
	}
	
	/**
	 * Returns true or false if the shape has expanded. Uses resizeBy internally.
	 */
	public boolean isExpanded(){
		if (resizeBy > 0)
			return true;
		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (this.x == x)
			return;
		
		stale = true;
		this.x = x;
	}
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (this.y == y)
			return;
		
		stale = true;
		this.y = y;
	}
	
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
}
