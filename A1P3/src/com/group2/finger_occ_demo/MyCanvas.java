package com.group2.finger_occ_demo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.group2.finger_occ_demo.States.FingerStates;
import com.group2.finger_occ_demo.activities.MovieActivity;
import com.group2.finger_occ_demo.activities.MovieSelectActivity;
import com.group2.finger_occ_demo.data.Movie;



public class MyCanvas extends View implements OnTouchListener, OnDragListener
{	
	public static final int MOVIE_VIEW_PROCESS = 1;
	public static final int MOVIE_SELECT_PROCESS = 2;
	public static Movie movieFound;
	public static ArrayList<Movie> moviesFound;
	ScatterPlotView scatterView;
	Display display;
	int screenWidth;
	int screenHeight;
	canvasApp context;

	AlertDialog mainDialog;
	public MyCanvas(canvasApp context){
		super(context);
        setFocusable(true);
        
        this.context = context;
                
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        this.setOnDragListener(this);
        
        setup();   
	}
	
	/**
	 * Dispatches onDraw to all views.
	 */
	public void onDraw(Canvas canvas) {
		scatterView.onDraw(canvas);
	}
	
	/**
	 * Dispatches onTouch to all views. invalidate bubbles down.
	 */
    public boolean onTouch(View view, MotionEvent event) {
    	ArrayList<Movie> movies = null;
    	FingerStates state;
    	float zoom;

    	// Feed the state machine
    	state = States.massageEvent(event);
    	
    	switch (state)
    	{
	    	case HOVER:
	    		scatterView.onTouch(event, view);
	    		break;

	    	case IDLE:
	    		movies = scatterView.getMovies(event);
	    		
	    		scatterView.invalidate();
	    		view.invalidate();
	    		break;
	    	
	    	case RESIZE:
	        	zoom = (float)States.zoom;
	        	
	        	if (zoom < 0.8f)
	        		scatterView.setZoom(0.8f);
	        	else
	        		scatterView.setZoom(zoom);	    		
	    		
	    		scatterView.invalidate();
	    		view.invalidate();
	    		break;
    	}
    	
    	// Handle swiping
    	switch (state)
    	{
    		case RESIZE:
    		case SWIPE:
    			scatterView.setOffset(States.screen_offset.x, States.screen_offset.y);
    			scatterView.invalidate();
    			view.invalidate();
    			break;
    	}
    	
    	// Open the Movie View Display
    	if (movies!=null){
    		if(movies.size() == 1){
        		movieFound = movies.get(0);// TODO Change later
    			context.startActivityForResult(new Intent(context, MovieActivity.class), MOVIE_VIEW_PROCESS);
    		}
    		else if(movies.size()>1){
    			moviesFound = movies;
    			context.startActivityForResult(new Intent(context, MovieSelectActivity.class), MOVIE_SELECT_PROCESS);
    		}
    	}
    	
    	
        return true;   
    }
    
    
    /**
     * Handle any callbacks from views
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MOVIE_VIEW_PROCESS || requestCode == 0)
        	scatterView.resetPoints(canvasApp.data.getMovie());
    }
    
    /**
     * Sends onDrag to each view.
     */
	public boolean onDrag(View view, DragEvent event) {
		scatterView.onDrag(event, view);
    	
		return true;
	}
    
	/**
	 * Creates views, and alert box.
	 */
	private void setup(){
		// Get screen dimensions
		display = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		// Create each view
		scatterView = new ScatterPlotView(screenWidth, screenHeight,  this.context);
	}
}