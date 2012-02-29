package com.group2.a1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
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
	int random;
	Paint paintStart;
	boolean startVisible;
	
	//Experiment Var
	String data;
	int maxTrials;
	int currTrial;
	
	Display display;
	int screenWidth;
	int screenHeight;
	
	int size;
	long timeStart;
	long timeEnd;
	long timeTotal;
	File file;
	int difficulty;
	boolean stop;
	
	int SS, SM, SL, MS, MM, ML, LS, LM, LL;

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
					timeStart = event.getEventTime();
					// randomly place target and show
					
	    			targetVisible = true;
					stop = false;
	    			while(stop == false){
	    			
	    				random = (int) (Math.random()*9);
				
	    				Log.w("test", Integer.toString(random));
					
						switch(random){
						
						case 0:
							
							if(SS < 10){
								size = 0;
								stop = true;
								SS++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								//Log.w("test", "SS");
								
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							break;
							
						case 1:
							if(SM < 10){
								size = 1;
								stop = true;
								SM++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							break;
							
						case 2:
							if(SL < 10){
								size = 2;
								stop = true;
								SL++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 3:
							if(MS < 10){
								size = 3;
								stop = true;
								MS++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 4:
							if(MM < 10){
								size = 4;
								stop = true;
								MM++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 5:
							if(ML < 10){
								size = 5;
								stop = true;
								ML++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 6:
							if(LS < 10){
								size = 6;
								stop = true;
								LS++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 7:
							if(LM < 10){
								size = 7;
								stop = true;
								LM++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 8:
							if(LL < 10){
								size = 9;
								stop = true;
								LL++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
						
							break;
						}
				
				}
					
	    			
	    		}//end if (START_TOUCHED)
				
    		}//end if (startVisible)
			
    		// target visible and hit
    		if(targetVisible == true)
    		{
    			if((event.getX() > rectLocX) && (event.getX() < rectLocX+rectWidth)
	    			&& (event.getY() > rectLocY) && (event.getY() < rectLocY+rectHeight))
	    		{
    				// record curr trial info
    				timeEnd = event.getEventTime();
    				timeTotal += timeEnd - timeStart;
					recordTrial((int)event.getX(), (int)event.getY(), timeEnd - timeStart, size);
					
					
    				
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
	    				
	    				try {
							saveToFile();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
		rectWidth = 75;
		rectHeight = 75;
		
		paintRect = new Paint();
		paintRect.setColor(Color.GREEN);
		paintRect.setAntiAlias(true);
		
		targetVisible = false;	

		//Rectangle (START)
		startLocX = 0;
		startLocY = 0;
		startWidth = 75;
		startHeight = 75;
		
		paintStart = new Paint();
		paintStart.setColor(Color.BLUE);
		paintStart.setAntiAlias(true);
		
		startVisible = true;
		rectLocX = 700;
		rectLocY = 350;
		//Experiment Var
		data = "'Time'," +  "'X'," + "'Y'," + "'Square Size'," + "'Finger'\n";
		maxTrials = 90;
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
    private void recordTrial(int x, int y, long touchTime, int size)
    {

    	data += touchTime + "," + x + "," + y  + "," + size + "\n";
    	Log.w("data", data);
    	
    }//end recordTrial
    
	/*
	 * saveToFile
	 * 
	 * Discrip:
	 */
	private void saveToFile()
	{
		
		try {
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "results.txt");
			FileOutputStream os = new FileOutputStream(file, true); 
		     OutputStreamWriter out = new OutputStreamWriter(os);
		     out.write(data);
		     out.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		


	}//end saveToFile
	

import android.view.WindowManager;

/*JF MODIFIED*/

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
	int random;
	Paint paintStart;
	boolean startVisible;
	
	//Experiment Var
	String data;
	int maxTrials;
	int currTrial;
	
	Display display;
	int screenWidth;
	int screenHeight;
	
	int size;
	long timeStart;
	long timeEnd;
	long timeTotal;
	File file;
	int difficulty;
	boolean stop;
	
	int SS, SM, SL, MS, MM, ML, LS, LM, LL;

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
					timeStart = event.getEventTime();
					// randomly place target and show
					
	    			targetVisible = true;
					stop = false;
	    			while(stop == false){
	    			
	    				random = (int) (Math.random()*9);
				
	    				Log.w("test", Integer.toString(random));
					
						switch(random){
						
						case 0:
							
							if(SS < 10){
								size = 0;
								stop = true;
								SS++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								//Log.w("test", "SS");
								
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							break;
							
						case 1:
							if(SM < 10){
								size = 1;
								stop = true;
								SM++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							break;
							
						case 2:
							if(SL < 10){
								size = 2;
								stop = true;
								SL++;
								currTrial++;
								rectWidth = 75;
								rectHeight = 75;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 3:
							if(MS < 10){
								size = 3;
								stop = true;
								MS++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 4:
							if(MM < 10){
								size = 4;
								stop = true;
								MM++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
							
						case 5:
							if(ML < 10){
								size = 5;
								stop = true;
								ML++;
								currTrial++;
								rectWidth = 100;
								rectHeight = 100;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 6:
							if(LS < 10){
								size = 6;
								stop = true;
								LS++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 400){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 7:
							if(LM < 10){
								size = 7;
								stop = true;
								LM++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 400 ||
										Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) > 800){	
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
							
							break;
						case 8:
							if(LL < 10){
								size = 9;
								stop = true;
								LL++;
								currTrial++;
								rectWidth = 125;
								rectHeight = 125;
								while(Math.sqrt(Math.pow(rectLocX, 2)+Math.pow(rectLocY, 2)) < 800){
									rectLocX = randNumGen(screenWidth-rectWidth-50);
									rectLocY = randNumGen(screenHeight-rectHeight-50);
								}
							}
						
							break;
						}
				
				}
					
	    			
	    		}//end if (START_TOUCHED)
				
    		}//end if (startVisible)
			
    		// target visible and hit
    		if(targetVisible == true)
    		{
    			if((event.getX() > rectLocX) && (event.getX() < rectLocX+rectWidth)
	    			&& (event.getY() > rectLocY) && (event.getY() < rectLocY+rectHeight))
	    		{
    				// record curr trial info
    				timeEnd = event.getEventTime();
    				timeTotal += timeEnd - timeStart;
					recordTrial((int)event.getX(), (int)event.getY(), timeEnd - timeStart, size);
					
					
    				
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
	    				
	    				try {
							saveToFile();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
		rectWidth = 75;
		rectHeight = 75;
		
		paintRect = new Paint();
		paintRect.setColor(Color.GREEN);
		paintRect.setAntiAlias(true);
		
		targetVisible = false;	

		//Rectangle (START)
		startLocX = 0;
		startLocY = 0;
		startWidth = 75;
		startHeight = 75;
		
		paintStart = new Paint();
		paintStart.setColor(Color.BLUE);
		paintStart.setAntiAlias(true);
		
		startVisible = true;
		rectLocX = 700;
		rectLocY = 350;
		//Experiment Var
		data = "'Time'," +  "'X'," + "'Y'," + "'Square Size'," + "'Finger'\n";
		maxTrials = 90;
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
    private void recordTrial(int x, int y, long touchTime, int size)
    {

    	data += touchTime + "," + x + "," + y  + "," + size + "\n";
    	Log.w("data", data);
    	
    }//end recordTrial
    
	/*
	 * saveToFile
	 * 
	 * Discrip:
	 */
	private void saveToFile()
	{
		
		try {
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "results.txt");
			FileOutputStream os = new FileOutputStream(file, true); 
		     OutputStreamWriter out = new OutputStreamWriter(os);
		     out.write(data);
		     out.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		


	}//end saveToFile
	
}