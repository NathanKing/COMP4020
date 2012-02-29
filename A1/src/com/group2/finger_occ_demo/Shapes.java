package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Responsible for all shapes on screen. Note order of list is the way of doing
 * z-indexing.
 */
public class Shapes {
	
	final int RECT_NUM = 30;
	
	private int[] rect_size = {20, 20};
	private ArrayList<Square_Shape> squares;
	private int radiusPX;// From the center of a shape
	private int screenWidth;
	private int screenHeight;
	
	public Shapes(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		// Derive rectangle size from screen size so it looks nice, but force sqaureness for now
		rect_size[0] = (int) (this.screenWidth * 0.05);
		rect_size[1] = (int) (this.screenWidth * 0.05);
		
		// Also derive radius from screen size, make it 4 times the shapes size
		radiusPX = rect_size[0] * 4;
		
		squares = new ArrayList<Square_Shape>();
		init_random();
	}
	
	/**
	 * Give starting coordinates for each rectangle in a random position.
	 */
	public void init_random(){
		// Omit first and last 2 squares
		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN};
		int color_num = 0;
		for (int i = 1; i < RECT_NUM; i++){
			squares.add(new Square_Shape(i + "", getR(screenWidth), getR(screenHeight), rect_size, colors[color_num]));
			
			color_num++;
			if (color_num == 3)
				color_num = 0;
		}
	}
	// gets a dimension, avoids anything within 10% of the borders
	private int getR(int screenDimen){
		int found = (int) ((Math.round(Math.random() * 100) * 0.01) * screenDimen);
		while (found > (screenWidth * 0.9) || found < (screenWidth * 0.1))
			found = (int) ((Math.round(Math.random() * 100) * 0.01) * screenDimen);
		return found;
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
