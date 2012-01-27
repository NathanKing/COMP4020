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
	
	Paint paintStart;
	boolean startVisible;
	
	//Experiment Var
	String data;
	int maxTrials;
	int currTrial;
	
	Display display;
	int screenWidth;
	int screenHeight;
	
	Workbook workbook;
	WritableWorkbook copy;
	File file;

	WritableSheet currentSheet;
	ArrayList datalist;
	/*
	 * Constructor
	 * 
	 * Discrip:
	 */
	public MyCanvas(Context context) throws RowsExceededException, WriteException
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

    				currTrial++;
    				if(currTrial % 5 == 0){
    					rectWidth += 25;
    					rectHeight += 25;
    					startWidth += 25;
    					startHeight += 25;
    				}
    				
    				if(currTrial == maxTrials / 2){
    					rectWidth = 75;
    					rectHeight = 75;
    					startWidth = 75;
    					startHeight = 75;
    				}
    		
    				
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
	private void setup() throws RowsExceededException, WriteException
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
		
		//Experiment Var
		data = "";
		maxTrials = 30;
		currTrial = 0;
		
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		datalist = new ArrayList<Data>();

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
    	int type = 0;
    	int finger = 0;
    	if(rectWidth < 100){
    		type = 0;
    	}
    	else if(rectWidth == 100){
    		type = 1;
    	}
    	else if(rectWidth == 125){
    		type = 2;
    	}
    	if(currTrial >= (maxTrials / 2)){
    		finger = 1;
    	}
    	Data newData = new Data(touchTime, x, y, type, finger);
    	datalist.add(newData);
    	//currentSheet.addCell(new Number(currentCol, currentRow, touchTime));
    	//currentSheet.addCell(new Number(currentCol+1, currentRow, x));
    	//currentSheet.addCell(new Number(currentCol+2, currentRow, y));
    	//currentRow++;
    	data += touchTime + " x=" + x + " y=" + y + "\n";
    	
    }//end recordTrial
    
	/*
	 * saveToFile
	 * 
	 * Discrip:
	 */
	private void saveToFile() throws RowsExceededException, WriteException
	{
		
		try {
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "results.xls");
			workbook = Workbook.getWorkbook(file);
			
			copy = Workbook.createWorkbook(file, workbook);

			currentSheet = copy.getSheet("index");

		} catch (Exception e) {
			
			try {
				copy = Workbook.createWorkbook(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "results.xls"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			currentSheet = copy.createSheet("index", 0);
			
			//currentSheet.addCell(new Label(0, 0, "Time Index1"));
			//currentSheet.addCell(new Label(1, 0, "X Index1"));
			//currentSheet.addCell(new Label(2, 0, "Y Index1"));
			
			//currentSheet.addCell(new Label(3, 0, "Time Index2"));
			//currentSheet.addCell(new Label(4, 0, "X Index2"));
			//currentSheet.addCell(new Label(5, 0, "Y Index2"));
			
			//currentSheet.addCell(new Label(6, 0, "Time Index3"));
			//currentSheet.addCell(new Label(7, 0, "X Index3"));
			//currentSheet.addCell(new Label(8, 0, "Y Index3"));
			
			//currentSheet = copy.createSheet("thumb", 1);
						
			//currentSheet.addCell(new Label(0, 0, "Time Thumb1"));
			//currentSheet.addCell(new Label(1, 0, "X Thumb1"));
			//currentSheet.addCell(new Label(2, 0, "Y Thumb1"));
			
			//currentSheet.addCell(new Label(3, 0, "Time Thumb2"));
			//currentSheet.addCell(new Label(4, 0, "X Thumb2"));
			//currentSheet.addCell(new Label(5, 0, "Y Thumb2"));
			
			//currentSheet.addCell(new Label(6, 0, "Time Thumb3"));
			//currentSheet.addCell(new Label(7, 0, "X Thumb3"));
			//currentSheet.addCell(new Label(8, 0, "Y Thumb3"));
			
			//currentSheet = copy.getSheet("index");
		}
		
		long time;
		int x, y, type, finger;
		Data data;
		int row;
		
		currentSheet = copy.createSheet(copy.getNumberOfSheets()+ "", copy.getNumberOfSheets());

		
		for(int i = 0; i < datalist.size(); i++){
			
			data = (Data)datalist.get(i);
			time = data.time;
			x = data.x;
			y = data.y;
			type = data.type;
			finger = data.finger;
			
			row = currentSheet.getRows();
			currentSheet.addCell(new Number(0, row, time));
			currentSheet.addCell(new Number(1, row, x));
			currentSheet.addCell(new Number(2, row, y));
			currentSheet.addCell(new Number(3, row, type));
			currentSheet.addCell(new Number(4, row, finger));
			
			
			Log.w("results", time + " " + x + " " + y + " " + type + " " + finger +"\n");
		}
		
		try {
			copy.write();
			copy.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}//end saveToFile
	
	private class Data{
	
		long time;
		int x, y, type, finger;
		
		public Data(long time, int x, int y, int type, int finger){
			
			this.time=time;
			this.x=x;
			this.y=y;
			this.type = type;
			this.finger = finger;
		}
	}

}