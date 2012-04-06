package com.group2.finger_occ_demo;

import java.util.Collection;
import java.util.LinkedList;

import com.group2.finger_occ_demo.data.Movie;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Tracks square size, location and color. Resizes square when in radius by in how much
 * in radius * 0.5 * 1 * current size.
 */
public class SquareShape {
	public LinkedList<Movie> movie = new LinkedList<Movie>();
	private int xDefault;
	private int yDefault;
	private int x;
	private int y;

	Paint color;
	Paint borderColor;
	private double resizeBy;//Computed factor to scale object by
	
	private boolean stale;				// Do we need to update these rects when we draw?
	private Rect shape = new Rect();
	private Rect border = new Rect();
	
	static private Rect		drawBoarder;	// Where to draw the movies on the screen
	static private Point2D	offset = new Point2D(0,0);
	
	// constants
	final private int BORDER = 2;//in pixels
	final private double resizeF = 0.07;//Constant to resize by {2 for device}

	static final int WIDTH = 20;
	static final int HEIGHT = 20;
	
	static final float RESIZE_FACTOR = 1.2f;	// How much more we should resize the squares
	
	// levels to display progressive info.
	final private double TEXT_APPEAR = 1.9;//times
	
	public SquareShape(Movie movie, int x, int y, int colorNum) {
		this.movie.add(movie);
		
		this.x = x;
		this.y = y;
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
	public void draw(Canvas on, float zoom)
	{
		Movie topMovie = movie.getFirst();
		
		if (stale)
		{
			int middle = WIDTH / 2;	// For now, we're using squares. Expand if using rectangles
			
			// Offset slightly above finger
			int offsetY  = (int) (-10 * resizeBy);
			int tempX;
			int tempY;
			
			middle *= 1 + resizeBy;
			
			tempX    = (int) (x *  8 * zoom);	// Pre-computing due to laziness
			tempY    = (int) (y * 35 * zoom);
			
			tempY	 = drawBoarder.bottom - (tempY + 25);	// 5 is to offset to the baseline
			
			shape.top    = tempY - middle + offsetY;
			shape.bottom = tempY + middle + offsetY;
			shape.left   = tempX - middle;
			shape.right  = tempX + middle;
			
			shape.offset(drawBoarder.left + (int)offset.x, drawBoarder.top + (int)offset.y);	// Offset starting position
			
			border.set(shape);
			border.top    += BORDER;
			border.bottom -= BORDER;
			border.left   += BORDER;
			border.right  -= BORDER;
			
			stale = false;
		}
		
		if (Rect.intersects(shape, drawBoarder))
		{
			on.drawRect(shape, borderColor);
			on.drawRect(border, color);

			// Decide on when to display extra data
			if (resizeBy > TEXT_APPEAR)
				on.drawText(topMovie.getTitle(), border.left + 3, border.top + 10, borderColor);			
		}
	}
	
	/**
	 * checks if the given coordinates are in the current shape.
	 */
	public boolean inShape(int[] position){
		return this.shape.contains(position[0], position[1]);
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
	
		resizeBy *= RESIZE_FACTOR;
		
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
		return (int) (WIDTH * resizeBy);
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
	
	public void invalidate()
	{
		stale = true;
		resizeBy = 0;
	}
	
	public Collection<Movie> getMovies() {
		return movie;
	}

	public void addMovie(Movie movie) {
		this.movie.add(movie);
	}

	public static Rect getDrawBoarder() {
		return drawBoarder;
	}

	public static void setOffset(float x, float y)
	{
		SquareShape.offset.x = x;
		SquareShape.offset.y = y;
	}
	
	public static void setDrawBoarder(Rect drawBoarder) {
		SquareShape.drawBoarder = drawBoarder;
	}
}
