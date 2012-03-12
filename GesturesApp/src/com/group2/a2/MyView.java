package com.group2.a2;

import java.util.ArrayList;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Display;
import android.view.WindowManager;

public class MyView extends GestureOverlayView{
	ArrayList<Shape>shapes = new ArrayList<Shape>();
	
	Paint white, red, blue, green;
	
	Paint paint;
	
	Display display;
	int screenWidth;
	int screenHeight;

	int menuLeftX;
	int menuRightX;
	int menuCenterX;
	int menuTopY;
	int menuBottomY;
	int menuCenterY;

	public MyView(Context context) {
		super(context);

		setup();
	}
	
	public void setup(){
		white = new Paint();
		white.setColor(Color.WHITE);
		white.setStyle(Paint.Style.FILL);
		white.setStrokeWidth((float) 10.0);
		
		green = new Paint();
		green.setColor(Color.GREEN);
		green.setStyle(Paint.Style.FILL);
		green.setStrokeWidth((float) 10.0);
		
		blue = new Paint();
		blue.setColor(Color.BLUE);
		blue.setStyle(Paint.Style.FILL);
		blue.setStrokeWidth((float) 10.0);
		
		red = new Paint();
		red.setColor(Color.RED);
		red.setStyle(Paint.Style.FILL);
		red.setStrokeWidth((float) 10.0);
		
		paint = red;
		
		
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		menuLeftX = 900;
		menuRightX = 1000;
		menuCenterX = menuRightX - (menuRightX - menuLeftX)/2;
		menuTopY = 0;
		menuBottomY = 550;
		menuCenterY = menuBottomY / 8;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas){
		
		//System.out.println("redraw");
		
		//this.
		
		RectF rect;
		Circle circle;
		Triangle tri;
		Line line;
		
		for(Shape shape:shapes){
			
			if(shape instanceof Rectangle){
				
				rect = (RectF) shape;
				
				canvas.drawRect(rect, paint);
			}
			else if(shape instanceof Circle){
				
				circle = (Circle) shape;
				
				canvas.drawCircle(circle.centerX(), circle.centerY(), circle.height()/2, paint);
			}
			else if(shape instanceof Triangle){
				
				float xmin, xmax;
				float ymin, ymax;
				
				xmin = shape.points[0];
				xmax = xmin;
				
				ymin = shape.points[1];
				ymax = ymin;
				
				for(int i = 0; i < shape.points.length; i++){
					
					//odd
					if(i % 2 != 0){
						if(shape.points[i] < ymin){
							ymin = shape.points[i];
						}
						else if(shape.points[i] > ymax){
							ymax = shape.points[i];
						}
					}
					//even
					else{
						if(shape.points[i] < xmin){
							xmin = shape.points[i];
						}
						else if(shape.points[i] > xmax){
							xmax = shape.points[i];
						}
					}
				}
				
				canvas.drawLine(xmin, ymax, xmax, ymax, paint);
				canvas.drawLine((xmin+xmax)/2, ymin, xmin, ymax, paint);
				canvas.drawLine((xmin+xmax)/2, ymin, xmax, ymax, paint);
			}
			else if(shape instanceof Line){
				
				line = (Line) shape;
				
				int size = line.points.length;
				
				canvas.drawLine(line.points[0], line.points[1], line.points[size-2], line.points[size-1], paint);
			}
			
		}
		
		canvas.drawLine(menuLeftX, -20, menuLeftX, 600, white);
	}
	
	public void deleteShape(RectF r, float[]points){
		
		int delete = -1;
		for(int i = 0; i < shapes.size(); i++){
			
			if(shapes.get(i).contains(r)){
				delete = i;
			}
		}
		if(delete!=-1){
			shapes.remove(delete);
		}
	
	}
}