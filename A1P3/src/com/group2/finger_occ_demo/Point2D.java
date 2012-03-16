package com.group2.finger_occ_demo;

public class Point2D {
	protected int x;
	protected int y;
	
	public double distance(Point2D dest){
		int a, b;
		
		a = Math.abs(x - dest.x);
		b = Math.abs(y - dest.y);
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
	
	public void translate(int x, int y)
	{
		this.x -= x;
		this.y -= y;
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
