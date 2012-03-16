package com.group2.finger_occ_demo;

public class Point2D {
	protected float x;
	protected float y;

	public Point2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double distance(Point2D dest){
		float a, b;
		
		a = Math.abs(x - dest.x);
		b = Math.abs(y - dest.y);
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
	
	public void translate(int x, int y)
	{
		this.x -= x;
		this.y -= y;
	}
	
	public float getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
