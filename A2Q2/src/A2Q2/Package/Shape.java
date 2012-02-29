package A2Q2.Package;

import android.graphics.Color;

public class Shape {
	double X;
	double Y;
	double prevX;
	double prevY;
	int colour; 
	int R;
	boolean selected = false;
	
	public Shape(double x,double y)
	{
		X = x;
		Y = y;
		colour = Color.rgb((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
	}
	
	public void select()
	{
		selected = true;
	}
	
	public void deselect()
	{
		selected = false;
	}
}
