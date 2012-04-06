package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.group2.finger_occ_demo.data.Movie;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for all shapes on screen. Note order of list is the way of doing
 * z-indexing.
 */
public class Points {
	private int[] rect_size = {20, 20};
	private ArrayList<SquareShape> squares;
	private int radiusPX;// From the center of a shape
	private float availableWidth;
	private float availableHeight;
	
	Rect squaresRegion = new Rect();	// Boarders for scatter plot
	private Point2D offset = new Point2D(0,0);
	
	private int[] xRange;
	private int[] yRange;
	
	//This controls when to invalidate the entire screen
	int INVALIDATE_POLL = 5;
	int invalCount = 0;
	
	private Comparator<SquareShape> sizeSorter;
	
	private Rect viewRect;
	private Paint viewRectPaint;
	
	/**
	 * Responsible for all shapes in a given box. Note order of list is the way of doing
	 * z-indexing.
	 */
	public Points(int xStart, int yStart, float screenWidth, float screenHeight, int[] xRange, int[] yRange){
		// Get the available width and ranges
		this.availableWidth = screenWidth;
		this.availableHeight = screenHeight;
		
		this.xRange = xRange;
		this.yRange = yRange;
		
		// Derive rectangle size from screen size so it looks nice (only for now)
		rect_size[0] = (int) (this.availableWidth * 0.02);
		rect_size[1] = (int) (this.availableWidth * 0.02);
		
		// Also derive radius from screen size, make it 4 times the shapes size
		radiusPX = rect_size[0] * 4;
		
		squares = new ArrayList<SquareShape>();
		
		init_from_data(null);
		
		sizeSorter = new Comparator<SquareShape>(){
			public int compare(SquareShape sqr1, SquareShape sqr2) {
				return (int)sqr1.resizeValue() - (int)sqr2.resizeValue();
			}
		};
		
		// Configure the plot boarder
		squaresRegion.top     = 25;
		squaresRegion.left    = 75;
		squaresRegion.bottom  = squaresRegion.top + 350;
		squaresRegion.right   = squaresRegion.left + 800;
		SquareShape.setDrawBoarder(squaresRegion);

		// Configure mini-map view boarder
		viewRect = new Rect(squaresRegion);
		viewRect.inset(360, 157);
		
		viewRectPaint = new Paint(Color.BLACK);
	}
	
	/**
	 * Currently uses year on x and rating on height. Formula is:
	 * (n/m) * r = x or y position,
	 * where n is the maximum graph value for the axis, n is the maximum value
	 * for the data points i.e. for rating 10, and r is the current value of the point.
	 * 
	 * Note1: If movies is null the default movie list is loaded with non filter user movies.
	 * Note2: If the current user possesses a movie with the same title that is current that
	 * 		  movie is used instead for the point.
	 */
	public void init_from_data(List<Movie> movies){
		if (movies == null)
			movies = canvasApp.data.getMovie();
		int heightInc = (int) (availableHeight/(yRange[1] - yRange[0]));
		int widthInc =  (int) (availableWidth/(xRange[1] - xRange[0]));
		Movie toUse;
		ArrayList<Movie> usedFromUser = new ArrayList<Movie>();
		
		squares = new ArrayList<SquareShape>();
		for (Movie movie : movies){
			// See if the current user has a modified version of the current movie
			toUse = checkCurUser(movie);
			if (!movie.equals(toUse))
				usedFromUser.add(toUse);
			
			// Don't draw the square as the user deleted it
			if (toUse != null)
				createSquare(heightInc, widthInc, toUse);
		}
		
		// Also must make sure any extra movies the user has are rendered.
		if (canvasApp.users.currentUser() != null){
			for (Movie movie : canvasApp.users.currentUser().getMovies()){
				if (movie.isDeleted() == false && !usedFromUser.contains(movie))
					createSquare(heightInc, widthInc, movie);
			}
		}
	}
	
	/**
	 * Draws graph lines (no ticks yet).
	 */
	public void drawGraph(Canvas canvas, float zoom){
		final int TEXT_OFFSET = 25;//tick label offset away from tick line, 0 means starts at tick line
		final int FROM_YEAR = 1900;
		Paint black = new Paint();
		black.setStrokeWidth(3);
		black.setFakeBoldText(true);
		
		final int XWIDTH		= 40;	// Width of x-axis tick marks in pixels
		final int YHEIGHT		= 35;	// Width of y-axis tick marks
		final int XSTEP			=  5;	// Change in X per step
		final int YSTEP			=  1;	// Change in Y per step
		
		float adjWidthX		= (XWIDTH  * zoom);
		float adjHeightY	= (YHEIGHT * zoom);
		
		// Draw boarders
		canvas.drawLine(squaresRegion.left,  squaresRegion.bottom, squaresRegion.right, squaresRegion.bottom, black);	// Bottom
		canvas.drawLine(squaresRegion.left,  squaresRegion.top,    squaresRegion.left,  squaresRegion.bottom, black);	// Left
		canvas.drawLine(squaresRegion.left,  squaresRegion.top,    squaresRegion.right, squaresRegion.top,    black);	// Top
		canvas.drawLine(squaresRegion.right, squaresRegion.top,    squaresRegion.right, squaresRegion.bottom, black);	// Right

		
		float loop;
		int text;
		float start;

		text = 0 + ((int)(offset.y / adjHeightY) * YSTEP);
		start  = squaresRegion.bottom;
		start += offset.y % adjHeightY;
		if (start > squaresRegion.bottom)
		{
			start -= adjHeightY;
			text  += YSTEP;
		}
		
		for (loop = start; loop >= squaresRegion.top; loop -= adjHeightY)
		{
			canvas.drawLine(squaresRegion.left - 10, loop, squaresRegion.left, loop, black);
			canvas.drawText(Integer.toString(text), squaresRegion.left - TEXT_OFFSET, loop + 5, black);
			text += YSTEP;
		}
		
		
		text = FROM_YEAR - ((int)(offset.x / (adjWidthX)) * XSTEP);
		start  = squaresRegion.left;
		start += offset.x % adjWidthX;
		if (start < squaresRegion.left)
		{
			start += adjWidthX;
			text  += XSTEP;
		}
		
		for (loop = start; loop <= squaresRegion.right + 1; loop += adjWidthX)
		{
			canvas.drawLine(loop, squaresRegion.bottom, loop, squaresRegion.bottom + 10, black);
			canvas.drawText(Integer.toString(text), loop - 10, squaresRegion.bottom + 25, black);
			text += XSTEP;
		}
	}	
	
	private void createSquare(float heightInc, float widthInc, Movie toUse){
		int x = toUse.getYear1900();
		int y = toUse.getRating();		//invert the ratings so 0 is at the bottom
		int color = getColor(toUse.getGenre().get(0));
		
		boolean found = false;
		
		// Don't add duplicates. My linear search is terrible...
		for (SquareShape square : squares)
		{
			if (square.getX() == x &&
				square.getY() == y)
			{
				found = true;
				square.addMovie(toUse);
				break;
			}
		}
		
		if (!found)
		{
			// Create initial square
			squares.add(new SquareShape(toUse, x, y, color));
		}
	}
	
	/**
	 * Checks if the current user has a movie if not returns the movie
	 * checking for if so returns users modified version of movie. Only when
	 * a user has deleted a movie it is returned as null.
	 */
	private Movie checkCurUser(Movie movie){
		User curUser = canvasApp.users.currentUser();
		
		if (curUser == null)
			return movie;
		else{
			Movie userMovie = curUser.tryGetMovie(movie.getTitle());
			if (userMovie == null)
				return movie;
			else if (userMovie.isDeleted() == false)
				return userMovie;
			else
				return null;
		}
	}
	
	/**
	 * Filters all movie points including users points. The users movies are removed then
	 * restored after filtering is done.
	 */
	public void filter_points(String genre, String rating){
		squares.clear();
		List<Movie> movies;
		List<Movie> userMoviesNew = new ArrayList<Movie>();
		ArrayList<Movie> userMovies = null;
		if (canvasApp.users.currentUser() != null)
			userMovies = (ArrayList<Movie>) canvasApp.users.currentUser().getMovies().clone();
		
		if(genre.equals("All") && rating.equals("All")){
			movies = canvasApp.data.getMovie();
			userMoviesNew = userMovies;
		}
		else{
			movies = new ArrayList<Movie>();
			if (canvasApp.users.currentUser() != null)
			userMoviesNew = canvasApp.users.currentUser().getMovies();
			
			movies = filter_movies(genre, rating, canvasApp.data.getMovie(), movies);
			userMoviesNew = filter_movies(genre, rating, userMoviesNew, userMoviesNew);
		}
		
		// Use the users updated movie list
		if (canvasApp.users.currentUser() != null)
			canvasApp.users.currentUser().setMovies((ArrayList<Movie>) userMoviesNew);
		
		init_from_data(movies);
		
		// Restore the users movies after they have been filtered.
		if (canvasApp.users.currentUser() != null)
			canvasApp.users.currentUser().setMovies(userMovies);
	}
	
	public List<Movie> filter_movies(String genre, String rating, List<Movie> iterateThrough,  List<Movie> movies){
		for(Movie movie : iterateThrough){
			
			String g = movie.getGenre().get(0);
			int r = movie.getRating();
			
			if(genre.equals("All")){
				if(r==Integer.valueOf(rating)){
					movies.add(movie);
				}
			}
			
			else if(rating.equals("All")){
				if(g.equals(genre)){
					movies.add(movie);
				}
			}
			
			else if(g.equals(genre) && r==Integer.valueOf(rating)){
				movies.add(movie);
			}
		}
		
		return movies;
	}
	
	public void filter_points(String text, String genre, String rating)
	{
		text = text.trim();
		
		if(text.length() >= 1){
			squares.clear();
			List<Movie> movies = new ArrayList<Movie>();
			List<Movie> userMoviesNew = new ArrayList<Movie>();
			ArrayList<Movie> userMovies = null;
			if (canvasApp.users.currentUser() != null){
				userMovies = (ArrayList<Movie>) canvasApp.users.currentUser().getMovies().clone();
				userMoviesNew = canvasApp.users.currentUser().getMovies();
			}
			
			movie_filter_search(text, genre, rating, canvasApp.data.getMovie(), movies);
			movie_filter_search(text, genre, rating, userMoviesNew, userMoviesNew);
		
			// Use the users updated movie list
			if (canvasApp.users.currentUser() != null)
				canvasApp.users.currentUser().setMovies((ArrayList<Movie>) userMoviesNew);
			
			init_from_data(movies);
			
			// Restore the users movies after they have been filtered.
			if (canvasApp.users.currentUser() != null)
				canvasApp.users.currentUser().setMovies(userMovies);
		}
		else
			init_from_data(canvasApp.data.getMovie());
	}
	
	/**
	 * With the given variables iterate through the list and put results into movies.
	 */
	public List<Movie> movie_filter_search(String text, String genre, String rating, List<Movie> iterateThrough,  List<Movie> movies){
		System.out.println(text);
		
		for(Movie movie : iterateThrough){
			
			String g = movie.getGenre().get(0);
			int r = movie.getRating();
			
			if(genre.equals("All") && rating.equals("All")){
				if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(movie.getTitle()).find())
					movies.add(movie);
			}
			else if(genre.equals("All")){
				if(r==Integer.valueOf(rating) && Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(movie.getTitle()).find())
					movies.add(movie);
			}
			
			else if(rating.equals("All")){
				if(g.equals(genre) && Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(movie.getTitle()).find())
					movies.add(movie);
			}
			
			else if(g.equals(genre) && r==Integer.valueOf(rating)){
				if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(movie.getTitle()).find())
					movies.add(movie);
			}
		}
		
		return movies;
	}
	
	/**
	 * Draw all shapes visible on the sent canvas. Due to ordering shapes in the same position end up in the
	 * same place of an array. So can use previous coordinates to determine if shape is drawable. Also sets
	 * the current shape to drawable.
	 */
	public void drawShapes(Canvas on, float zoom){
		// Make sure we don't draw where we aren't supposed to
		on.save();
		on.clipRect(squaresRegion);
		
		for (SquareShape square : squares){
			square.draw(on, zoom);
		}
		
		on.restore();
	}
	
	/**
	 * Draw mini-map
	 * @param on
	 * @param zoom
	 */
	public void drawPoints(Canvas on, float zoom)
	{
		for (SquareShape square : squares){
			square.drawPoint(on, zoom, viewRect);
		}		
	}
	
	public void drawViewRect(Canvas canvas, float zoom)
	{
		canvas.drawLine(viewRect.left,  viewRect.bottom, viewRect.right, viewRect.bottom, viewRectPaint);	// Bottom
		canvas.drawLine(viewRect.left,  viewRect.top,    viewRect.left,  viewRect.bottom, viewRectPaint);	// Left
		canvas.drawLine(viewRect.left,  viewRect.top,    viewRect.right, viewRect.top,    viewRectPaint);	// Top
		canvas.drawLine(viewRect.right, viewRect.top,    viewRect.right, viewRect.bottom, viewRectPaint);	// Right
	}
	
	/**
	 * If in any drawn shape on the canvas is under the finger and is the biggest.
	 */
	public ArrayList<Movie> inShape(int x, int y){
		SquareShape square;
		int[] position = {x, y};
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		
		// Grab the largest movies currently selected
		// See if in any of the shapes, backwards loop because current biggest
		// objects are in the end of the array list.
		
		double largest = squares.get(squares.size() - 1).resizeValue();

		// If none can be considered selected
		if (largest == 0)
			return movies;
		
		if (squares.size() > 1){
			for (int i = squares.size() - 1; i > 0; i--){
				square = squares.get(i);
				if (square.inShape(position) == true){
					System.out.println("in shape");
					movies.addAll(square.getMovies());
				}
				
				// Give up on anything smaller than what's selected
				if (square.resizeValue() < largest)
					break;
			}
		}
		else
			movies.addAll(squares.get(0).getMovies());
		
		return movies;
	}
	
	/**
	 * Checks an expands any shapes in the current radius. Does this by specifying and area
	 * that will need to be redrawn. The biggest objects are put in front.
	 */
	public void checkRadius(int x, int y, View view){
		for (SquareShape square : squares)
			square.checkRadius(x, y, radiusPX);
		
		view.invalidate();
		
		reorderZ();
	}
	
	private void reorderZ()
	{
		Collections.sort(squares, sizeSorter);
	}
	
	/**
	 * Sets all shapes to the default size
	 */
	public void goDefaultSize(){
		for (int i = 0; i < squares.size(); i++)
			squares.get(i).setDefaultSize();
	}
	
	/*
	 * Getters and setters
	 */
	
	public void resetPosition(){
		for (SquareShape square : squares)
			square.resetPosition();
	}
	
	public void invalidate()
	{
		for (SquareShape square : squares)
			square.invalidate();
	}
	
	public void setOffset(float x, float y)
	{
		offset.x = x;
		offset.y = y;
	}
	
	public int getColor(String color){
		
		if(color.equals("Action")){
			return Color.LTGRAY;
		}
		else if(color.equals("Drama")){
			return Color.RED;	
		}
		else if(color.equals("Mystery")){
			return Color.BLUE;
		}
		else if(color.equals("Comedy")){
			return Color.MAGENTA;
		}
		else if(color.equals("Music")){
			return Color.YELLOW;
		}
		else if(color.equals("War")){
			return Color.GREEN;
		}
		else if(color.equals("Sci-Fi")){
			return Color.rgb(255, 69, 0);//orange
		}
		else if(color.equals("Western")){
			return Color.rgb(238, 221, 130);
		}
		else if(color.equals("Horror")){
			return Color.rgb(152, 251, 152);
		}
		else if(color.equals("Family")){
			return Color.rgb(160, 32, 240);
		}
		else if(color.equals("Fantasy")){
			return Color.rgb(224, 255, 255);
		}
		else if(color.equals("Romance")){
			return Color.rgb(255, 20, 147);
		}
		else if(color.equals("Short")){
			return Color.GRAY;
		}
		else if(color.equals("Thriller")){
			return Color.rgb(178, 34, 34);
		}
		return Color.argb(200, 200, 0, 250);
	}
}
