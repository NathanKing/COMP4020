package com.group2.a2;

import android.graphics.RectF;

class Shape extends RectF{
	public float[]points;
	public Shape(RectF r, float[]points){
		
		super(r);
		this.points=points;
	}
}

class Triangle extends Shape{

	public Triangle(RectF r, float[]points) {
		super(r, points);
	}
}

class Rectangle extends Shape{

	public Rectangle(RectF r, float[]points) {
		super(r, points);
	}
}

class Line extends Shape{
	
	public Line(RectF r, float[]points) {
		super(r, points);
	}
	
}

class Circle extends Shape{

	public Circle(RectF r, float[]points) {
		super(r, points);
	}
}