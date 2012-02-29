package A2Q2.Package;

import java.sql.Date;
import java.util.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.Settings.System;
import android.util.Log;
import android.view.WindowManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyCanvas extends View implements OnTouchListener
{
	int currBall = 0;
	ArrayList<Shape> shapes = new ArrayList();
	ArrayList<Shape> explodeTargets = new ArrayList();
    
    int ht;
    int wd;	
	
	//Constructor
	public MyCanvas(Context context)
	{
		super(context);
        setFocusable(true);
        
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        
        ht = display.getHeight();
        wd = display.getWidth();
        
        setup();
        
	}//end constructor
	
	public void onDraw(Canvas canvas) 
	{	
		super.onDraw(canvas);
		Paint colour = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		for(int i = 0; i < shapes.size(); i++)
		{
			if(!shapes.get(i).selected)
				colour.setColor(Color.BLACK);
			else
				colour.setColor(Color.WHITE);
			
			if(shapes.get(i).getClass() == Circle.class)
			{
				canvas.drawCircle((float)shapes.get(i).X, (float)shapes.get(i).Y, ((Circle)shapes.get(i)).R, colour);
			
				colour.setColor(shapes.get(i).colour);
				canvas.drawCircle((float)shapes.get(i).X, (float)shapes.get(i).Y, ((Circle)shapes.get(i)).R-3, colour);
			}
			
			else if(shapes.get(i).getClass() == Rect.class)
			{
				canvas.drawRect((float)shapes.get(i).X, (float)shapes.get(i).Y, (float)(shapes.get(i).X + ((Rect)shapes.get(i)).length)  ,(float)(shapes.get(i).Y + ((Rect)shapes.get(i)).length), colour);
				
				colour.setColor(shapes.get(i).colour);
				canvas.drawRect((float)shapes.get(i).X + 3, (float)shapes.get(i).Y +3, (float)(shapes.get(i).X + ((Rect)shapes.get(i)).length - 3)  ,(float)(shapes.get(i).Y + ((Rect)shapes.get(i)).length - 3), colour);
			} 
		}
		
		if(explodeTargets.size() >= 2)
		{
			for(int i = 0; i < explodeTargets.size(); i++)
			{
				colour.setColor(Color.WHITE);
				canvas.drawLine((float)explodeTargets.get(i).X, (float)explodeTargets.get(i).Y, (float)explodeTargets.get(i).prevX, (float)explodeTargets.get(i).prevY, colour);
			}
		}
	}
	
	private void setup()
	{
		//create Red balls
		for(int i = 0; i < 6; i++)
		{
			shapes.add(new Circle(Math.abs(Math.random()*wd - 100), Math.abs(Math.random()* ht- 100)));
		}
		
		for(int i = 0; i < 6; i++)
		{
			shapes.add(new Rect(Math.abs(Math.random()*wd - 100), Math.abs(Math.random()* ht- 100)));
		}

		this.invalidate();
	}
	
	public boolean distance(MotionEvent event, Circle b)
	{
		double X = Math.abs(event.getX() - b.X);
		double Y = Math.abs(event.getY() - b.Y);
		
		return Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2)) < b.R;
	}
	
	public boolean distance(MotionEvent event, Rect b)
	{
		boolean intersects = false;
		
		if(event.getX() >= b.X && event.getX() <= b.X + b.length && event.getY() >= b.Y && event.getY() <= b.Y + b.width)
			intersects = true;
		
		return intersects;
	}
	
	public boolean distance(MotionEvent event, Shape b)
	{
		if(b.getClass() == Circle.class)
			return distance(event, (Circle)b);
		else
			return distance(event, (Rect)b);
	}
	
	public boolean onTouch(View view, MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			Log.w("Targets", Integer.toString(explodeTargets.size()));
			if(explodeTargets.size() >= 2)
			{
				
				for(int i = 0; i < explodeTargets.size(); i++)
				{
					if(distance(event, explodeTargets.get(i)))
					{	
						if(explodeTargets.get(i).selected)
							explodeTargets.get(i).deselect();
						else
							explodeTargets.get(i).select();
						break;
					}
				}
				
				
				for(int i = 0; i < explodeTargets.size(); i++)
				{
					UnExplode(explodeTargets.get(i),i);
				}
				
			}
			explodeTargets.clear();
			
		}
		else
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN)
		{		
			for(int i = shapes.size()-1; i >= 0; i--)
			{
				
				if(explodeTargets.size() == 0)
				{
					shapes.get(i).deselect();
					CheckMode(event);		

					if(explodeTargets.size() >=2)
						ExplodeTargets(event);
				}
				else
				{
					if(distance(event, shapes.get(i)))
					{
						//move the ball to the end so that it shows up over the others
						if(shapes.get(i).selected)
							shapes.get(i).deselect();
						else
							shapes.get(i).select();
						break;
					}
				}
			}
		}
		
		this.invalidate();
		
        return true;
	}
	
	public void ExplodeTargets(MotionEvent event)
	{
		//not doing anything with count yet
		
		for(int i = 0; i < explodeTargets.size(); i++)
		{
			Explode(explodeTargets.get(i), i);
		}
	}
	
	public void Explode(Shape move, int i)
	{
		boolean onscreen = false;
		
		while(!onscreen)
		{
			switch(i % 4)
			{
				case 1:
					if((move.X + move.R) < ht && (move.Y + move.R) < wd)
					{
						move.prevX = move.X;
						move.prevY = move.Y;
						move.X += move.R;
						move.Y += move.R;
						onscreen = true;
						
					}
					else
					{
						i++;
					}
					break;
				case 0:
					if((move.X + move.R) < ht && (move.Y - move.R) > 0)
					{
						move.prevX = move.X;
						move.prevY = move.Y;
						move.X += move.R;
						move.Y -= move.R;
						onscreen = true;
						
					}
					else
					{
						i++;
					}
					break;
				case 3:
					if((move.X - move.R) > 0 && (move.Y - move.R) > 0)
					{
						move.prevX = move.X;
						move.prevY = move.Y;
						move.X -= move.R;
						move.Y -= move.R;
						onscreen = true;
						
					}
					else
					{
						i++;
					}
					break;
				case 2:
					if((move.X - move.R) > 0 && (move.Y + move.R) < wd)
					{
						move.prevX = move.X;
						move.prevY = move.Y;
						move.X -= move.R;
						move.Y += move.R;
						onscreen = true;
						
					}
					else
					{
						i++;
					}
					break;
			}	
		}
	}
	
	public void UnExplode(Shape move, int i)
	{
		move.X = move.prevX;
		move.Y = move.prevY;
	}
	
	public void CheckMode(MotionEvent event)
	{
		explodeTargets.clear();
		
		for(int i = 0; i < shapes.size(); i++)
		{
			if(distance(event, shapes.get(i)))
			{
				explodeTargets.add(shapes.get(i));
			}
		}
	}
	
}
