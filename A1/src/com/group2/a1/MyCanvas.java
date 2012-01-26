package com.group2.a1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;


public class MyCanvas extends View implements OnTouchListener
{
	//Rectangle (TARGET)
	int rectLocX;
	int rectLocY ;
	int rectWidth;
	int rectHeight;	
	
	Paint paintRect;
	boolean targetVisible;
	
	//Rectangle (START)
	int startLocX;
	int startLocY;
	int startWidth;
	int startHeight;
	
	Paint paintStart;
	boolean startVisible;
	
	//Experiment Var
	String data;
	int maxTrials;
	int currTrial;
	
	Display display;
	int screenWidth;
	int screenHeight;
	
	/*
	 * Constructor
	 * 
	 * Discrip:
	 */
	public MyCanvas(Context context)
	{
		super(context);
        setFocusable(true);
        
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        
        setup();
        
	}//end constructor
	
	/*
	 * onDraw
	 * 
	 * Discrip:
	 */
	public void onDraw(Canvas canvas) 
	{	

		if(startVisible == true)
			canvas.drawRect(startLocX, startLocY, startLocX+startWidth, startLocY+startHeight, paintStart);

		if(targetVisible == true)
			canvas.drawRect(rectLocX, rectLocY, rectLocX+rectWidth, rectLocY+rectHeight, paintRect);

	}//end onDraw	
	
	/*
	 * onTouch
	 * 
	 * Discrip:
	 */
    public boolean onTouch(View view, MotionEvent event) 
    {
    	if(event.getAction() == MotionEvent.ACTION_UP )
    	{ 
    		// start visible and hit
    		if(startVisible == true)
    		{
				if((event.getX() > startLocX) && (event.getX() < startLocX+startWidth)
	    			&& (event.getY() > startLocY) && (event.getY() < startLocY+startHeight))
	    		{
					startVisible = false;
					
					// randomly place target and show
	    			rectLocX = randNumGen(screenWidth-rectWidth);
	    			rectLocY = randNumGen(screenHeight-50-rectHeight);
	    			targetVisible = true;
	    			
	    		}//end if (START_TOUCHED)
				
    		}//end if (startVisible)
			
    		// target visible and hit
    		if(targetVisible == true)
    		{
    			if((event.getX() > rectLocX) && (event.getX() < rectLocX+rectWidth)
	    			&& (event.getY() > rectLocY) && (event.getY() < rectLocY+rectHeight))
	    		{
    				// record curr trial info
    				recordTrial((int)event.getX(), (int)event.getY(), event.getEventTime());
    				  				
    				// move to next trial
    				currTrial++;
    				
    				// reset, show start
    				if(currTrial < maxTrials)
    				{
	    				targetVisible = false;
	    				startVisible = true;
    				}//end if
    				
    				// finish, save data
    				else
    				{
	    				targetVisible = false;
	    				startVisible = false;	
	    				
	    				saveToFile(data);
    				}//end if-else
    				
	    		}//end if (RECT_TOUCHED)
    			
    		}//end if(targetVisible)
    
    	}//end if (UP) 

		this.invalidate();
		
        return true;
        
    }//end onTouch
    
	/*
	 * setup
	 * 
	 * Discrip:
	 */
	private void setup()
	{
		//Rectangle (TARGET)
		rectLocX = 0;
		rectLocY = 0;
		rectWidth = 100;
		rectHeight = 100;	
		
		paintRect = new Paint();
		paintRect.setColor(Color.GREEN);
		paintRect.setAntiAlias(true);
		
		targetVisible = false;	

		//Rectangle (START)
		startLocX = 0;
		startLocY = 0;
		startWidth = 100;
		startHeight = 100;
		
		paintStart = new Paint();
		paintStart.setColor(Color.BLUE);
		paintStart.setAntiAlias(true);
		
		startVisible = true;
		
		//Experiment Var
		data = "";
		maxTrials = 5;
		currTrial = 0;
		
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		
		
	}//end setup
    
	/*
	 * randNumGen
	 * 
	 * Discrip:
	 */
    private int randNumGen(int max)
    {    	  	
    	return (int)(Math.random()* max);
    	
    }//end randNumGen
    
	/*
	 * recordTrial
	 * 
	 * Discrip:
	 */
    private void recordTrial(int x, int y, long touchTime)
    {
    	data += touchTime + " x=" + x + " y=" + y + " ";
    	
    }//end recordTrial
    
	/*
	 * saveToFile
	 * 
	 * Discrip:
	 */
	private void saveToFile(String dataToSave)
	{
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "results.txt");

		try {

	        FileOutputStream os = new FileOutputStream(file); 
	        OutputStreamWriter out = new OutputStreamWriter(os);
	        
	        //if(Environment.)

	        out.write(dataToSave);
	        out.close();
	     

	    } catch (Exception e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }

	}//end saveToFile

}