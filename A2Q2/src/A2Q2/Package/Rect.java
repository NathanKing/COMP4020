package A2Q2.Package;

public class Rect extends Shape{
	int length;
	int width;
	
	public Rect(double x, double y)
	{
		super(x,y);
		length = (int)((Math.random()*80) + 40);
		width = (int)((Math.random()*80) + 40);
		R = (int)Math.sqrt(Math.pow(length, 2) + Math.pow(width, 2));
	}
}
