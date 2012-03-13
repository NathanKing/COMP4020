package com.group2.finger_occ_demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Tracks square size, location and color. Resizes square when in radius by in how much
 * in radius * 0.5 * 1 * current size.
 */
public class Square_Shape {
	public String name;
	private int xDefault;
	private int yDefault;
	private int x;
	private int y;
	private int[] size;
	Paint color;
	private double resizeBy;//Computed factor to scale object by
	
	// constants
	final private int BORDER = 2;//in pixels
	final private double resizeF = 2;//Constant to resize by {2 for device}
	
	Square_Shape(String name, float x, float y, int[] size, int colorNum) {
		this.name  = name;
		
		this.x = (int) x;
		this.y = (int) y;
		this.size = size;
		this.resizeBy = 0;
		
		color = new Paint();
		color.setColor(colorNum);
		color.setAntiAlias(true);
	}
	
	/**
	 * Draw rectangle on current canvas with text denoting its number, resize from center if necessary. Contains
	 * a black border.
	 */
	public void draw(Canvas on){
		int expandByX = (int) (size[0] * (resizeBy*0.5));
		int expandByY = (int) (size[1] * (resizeBy*0.5));
		on.drawRect(getX() - expandByX, y - expandByY, getX() + size[0] + expandByX, y + size[1] + expandByY, new Paint(Color.BLACK));
		on.drawRect(getX() - expandByX + BORDER, y - expandByY + BORDER, getX() + size[0] + expandByX - BORDER, y + size[1] + expandByY - BORDER, color);
		
		//on.drawText(name, x - expandByX + 3 + BORDER, y - expandByY + 10 + BORDER, new Paint(Color.BLACK));
	}
	
	/**
	 * checks if the given coordinates are in the current shape.
	 */
	public boolean inShape(int[] position){
		int expandByX = (int) (size[0] * (resizeBy*0.5));
		int expandByY = (int) (size[1] * (resizeBy*0.5));
		return ((position[0] > getX() - expandByX) && (position[0] < getX() + size[0] + expandByX && (position[1] > y - expandByY) && (position[1] < y + size[1] + expandByY) ));
	}
	
	/**color
	 * Checks the current square is in the circle with the specified center and radius. Does this
	 * by comparing vector between circle and each of the squares 4 points.
	 */
	public void checkRadius(int circle_x, int circle_y, int radius){
		// All points for the current square
		int[][] points = {{getX(), y}, 
						  {getX() + size[0], y},
						  {getX(), y + size[1]},
						  {getX() + size[0], y + size[1]}};
		
		// Loop though all points checking if the vector from the circle to the square
		// is longer than the circle radius (no intersection) and by how much.
		int f;
		int e;
		int vector = Integer.MAX_VALUE;
		int maxDiff = 0;
		for (int point = 0; point < points.length; point++){
			f = Math.abs(circle_x - points[point][0]);
			e = Math.abs(circle_y - points[point][1]);
			vector = (int) Math.sqrt(Math.pow(f, 2) + Math.pow(e, 2));
			
			if (radius - vector > maxDiff)
				maxDiff = radius - vector;
		}
		
		// resize square if circle was in radius
		if (maxDiff != 0)
			resizeBy = (maxDiff * (resizeF * 0.01));
	}
	
	public void translate(int xOffset, int yOffset){
		this.x += xOffset;
		this.y += yOffset;
	}
	
	/**
	 * Set resizeBy to 1 making the shapes size normal.
	 */
	public void setDefaultSize(){
		resizeBy = 0;
	}
	
	/**
	 * Reset sqaure to the position it originally displayed in.
	 */
	public void resetPosition() {
		x = xDefault;
		y = yDefault;
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
