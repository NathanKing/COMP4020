package A2Q2.Package;

public class Circle extends Shape {
	
	public Circle(double x, double y)
	{
		super(x, y);
		R = (int)((Math.random()*80) + 40);
	}

}
