package com.group2.finger_occ_demo.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.group2.finger_occ_demo.MyCanvas;
import com.group2.finger_occ_demo.R;
import com.group2.finger_occ_demo.canvasApp;
import com.group2.finger_occ_demo.R.id;
import com.group2.finger_occ_demo.R.layout;
import com.group2.finger_occ_demo.data.Movie;

/**
 * Shows the movie view
 */
public class MovieActivity extends Activity {
	final String NO_CERT = "N/A";
	final String TIME_UNITS = "min";
	
	private Movie movieFound;
	
	// Buttons
	private Button favoriteButton;
	private Button saveButton;
	private Button backButton;
	private Button deleteButton;
	
	// Editable Fields
	private TextView titleText;
	private EditText yearText;
	private EditText directorText;
	private EditText lengthText;
	private EditText certificationText;
	private EditText actorText;
	private EditText genreText;
	private RatingBar ratingBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Set up layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielayout);

        // Set the info
        movieFound = MyCanvas.movieFound;
        setMovieInfo();
        
        // Favorites button just adds string of movie to favorites which is later indexed
        favoriteButton = (Button)findViewById(R.id.favouritesButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveFavourite();
            }
		});
        
        // Save button currently changes the entry based on the currently set fields
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveMovieInfo();
            }
		});
        
        // Back button returns to main screen in default state
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
            }
			
		});
        
        deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleteMovie();
            }
		});
    }
    
    /**
     * Read in all info into the GUI via the movie that was clicked on for this.
     */
    private void setMovieInfo(){
    	String list;
    	
    	// Modify top text fields
    	titleText = (TextView)findViewById(R.id.titleText);
    	titleText.setText(movieFound.getTitle());
    	
    	yearText = (EditText)findViewById(R.id.yearText);
    	yearText.setText(movieFound.getYear() + "");
    	
    	directorText = (EditText)findViewById(R.id.directorText);
    	directorText.setText(movieFound.getDirector());
    	
    	lengthText = (EditText)findViewById(R.id.lengthText);
    	lengthText.setText(movieFound.getLength());
    	
    	certificationText = (EditText)findViewById(R.id.certificationText);
    	certificationText.setText(movieFound.getCertification());
    	if (movieFound.getCertification() == null || movieFound.getCertification().equals(""))
    		certificationText.setText(NO_CERT);
    	
    	// Modify actor and genre lists
    	list = "";
    	for (String actor : movieFound.getActors())
    		list += actor.trim() + ", ";
    	actorText = (EditText)findViewById(R.id.actorText);
    	actorText.setText(list.substring(0, list.length()-2));// The -2 gets rid of extra comma and space
    			
		list = "";
    	for (String genre : movieFound.getGenre())
    		list += genre.trim() + ", ";
    	genreText = (EditText)findViewById(R.id.genresText);
    	genreText.setText(list.substring(0, list.length()-2));// The -2 gets rid of extra comma and space
    	
    	ratingBar = (RatingBar)findViewById(R.id.ratingBar1);
    	ratingBar.setRating(movieFound.getRating());
    }
    
    /**
     * Save all info store in the current text fields for the users movie list if there is a user. Otherwise
     * store all information globally.
     */
    private void saveMovieInfo(){
    	Movie userMovie;
    	if (canvasApp.users.currentUser() == null)
    		userMovie = movieFound;
    	else
    		userMovie = new Movie();
    	
    	// Extract actor and genre lists, in Python this would be 2 lines...
    	ArrayList<String> actorsL = new ArrayList<String>();
    	ArrayList<String> genresL = new ArrayList<String>();
    	String[] actors = actorText.getText().toString().split(",");
    	for (String actor : actors)
    		actorsL.add(actor.trim());
    	String[] genres = genreText.getText().toString().split(",");
    	for (String genre : genres)
    		genresL.add(genre.trim());
    	
    	// If there is no time unit added default to default time
    	try {
    		Integer.parseInt(lengthText.getText().toString().trim());
    		lengthText.setText(lengthText.getText().toString() + " " + TIME_UNITS);
    	}
    	// catch indicates a valid case
    	catch (NumberFormatException e){}
    	
    	// Save top text fields
    	userMovie.setDirector(directorText.getText() + "");
    	userMovie.setTitle(titleText.getText() + "");
    	userMovie.setYear(Integer.parseInt(yearText.getText().toString()));
    	userMovie.setCertification(certificationText.getText() + "");
    	userMovie.setLength(lengthText.getText() + "");
    	userMovie.setActors(actorsL);
    	userMovie.setGenre(genresL);
    	userMovie.setRating((int) ratingBar.getRating());
    	
    	System.out.println("Saving...: " + canvasApp.users.currentUser());
    	if (canvasApp.users.currentUser() != null)
    		canvasApp.users.currentUser().addMovie(userMovie);
    }
    
    /**
     * Add this movie to the list of user favorites if it isn't already there. Does
     * nothing if there is no user.
     */
    public void saveFavourite(){
    	if (canvasApp.users.currentUser() != null)
    		canvasApp.users.currentUser().addFavourite(movieFound.getTitle());
    }
    
    /**
     * Delete the current movie from the user if there is a user. If no user nothing happens.
     * Note the movie will still exist.
     */
    public void deleteMovie(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	
    	// Pop up alert for delete movie
    	if (canvasApp.users.currentUser() != null){
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
	        	        // delete the movie and exit this activity
	        	        case DialogInterface.BUTTON_POSITIVE:
	        	        	canvasApp.users.currentUser().deleteMovie(movieFound.getTitle());
	        	        	finish();
	        	            break;
	        	        // Do nothing on cancel (or no)
	        	        case DialogInterface.BUTTON_NEGATIVE:
	        	            break;
        	        }
        	    }
        	};
    		
    		alert.setMessage("Are you sure you want to permanently delete " + movieFound.getTitle() + "?");
    		alert.setPositiveButton("Yes", dialogClickListener);
    	    alert.setNegativeButton("No", dialogClickListener);
    		
    		alert.show();
    	}
    }
}