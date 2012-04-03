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
	
	private int[] xRange;
	private int[] yRange;
	
	//This controls when to invalidate the entire screen
	int INVALIDATE_POLL = 5;
	int invalCount = 0;
	
	private Comparator<SquareShape> sizeSorter;
	
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
		
		// Configure the plot boarder
		squaresRegion.top     = 25;
		squaresRegion.left    = 75;
		squaresRegion.bottom  = squaresRegion.top + 350;
		squaresRegion.right   = squaresRegion.left + 800;
		SquareShape.setDrawBoarder(squaresRegion);
		
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
	public void drawGraph(Canvas canvas){
		final int TEXT_OFFSET = -15;//tick label offset away from tick line, 0 means starts at tick line
		final int FROM_YEAR = 1900;
		Paint black = new Paint();
		black.setStrokeWidth(3);

		// Draw boarders
		canvas.drawLine(squaresRegion.left, squaresRegion.bottom, squaresRegion.right, squaresRegion.bottom, black);	// Bottom
		canvas.drawLine(squaresRegion.left, squaresRegion.top,    squaresRegion.left,  squaresRegion.bottom, black);	// Left
	}	
	
	private void createSquare(float heightInc, float widthInc, Movie toUse){
		int x = toUse.getYear1900();
		int y = 10 - toUse.getRating();		//invert the ratings so 0 is at the bottom
		int color = getColor(toUse.getGenre().get(0));
		
		// Don't add duplicates. Linear search is terrible...
		for (SquareShape square : squares)
		{
			if (square.getX() == x &&
				square.getY() == y)
			{
				// Add to movie list in a square
			}
			else
			{
				// Create initial suqare
				squares.add(new SquareShape(toUse, x, y, color));
			}
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
	public void drawShapes(Canvas on){
		for (SquareShape square : squares){
			square.draw(on);
		}
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
					movies.add(square.getMovie());
				}
				
				// Give up on anything smaller than what's selected
				if (square.resizeValue() < largest)
					break;
			}
		}
		else
			movies.add(squares.get(0).getMovie());
		
		return movies;
	}
	
	public void checkRadius(int x, int y, View view){
		// invalidate only this rectangle.
		if (invalCount == INVALIDATE_POLL){
			invalCount = 0;
			checkRadiusFull(x, y, view);
		}
		else
			checkRadiusSimple(x, y, view);
		invalCount += 1;
	}
	
	/**
	 * Checks an expands any shapes in the current radius. Does this by specifying and area
	 * that will need to be redrawn. The biggest objects are put in front.
	 */
	public void checkRadiusFull(int x, int y, View view){
		for (SquareShape square : squares)
			square.checkRadius(x, y, radiusPX);		
		
		view.invalidate();
	}
	
	/**
	 * Checks an expands any shapes in the current radius. Does this by specifying and area
	 * that will need to be redrawn. The biggest objects are put in front.
	 */
	public void checkRadiusSimple(int x, int y, View view){
		int largeRadius = (int) (radiusPX * 1.6);
		for (SquareShape square : squares){
			// if a square is double the distance away don't bother calculating it. This is a rough approximation keep in mind.
			if ( (square.getX() > x - largeRadius && square.getX() < x + largeRadius) &&
				 (square.getY() > y - largeRadius && square.getY() < y + largeRadius))
				square.checkRadius(x, y, radiusPX);
		}
		
		// invalidate only this rectangle.
		view.invalidate(x - largeRadius, y - largeRadius, x + largeRadius, y + largeRadius);
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
	
	public int getColor(String color){
		
		if(color.equals("Action")){
			return Color.WHITE;
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
		return Color.WHITE;
	}
}
