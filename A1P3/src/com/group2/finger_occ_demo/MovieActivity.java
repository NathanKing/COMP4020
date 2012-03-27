package com.group2.finger_occ_demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.group2.finger_occ_demo.data.Movie;

/**
 * Shows the movie view
 */
public class MovieActivity extends Activity {
	final String NO_CERT = "N/A";
	final String TIME_UNITS = "min";
	
	private Movie movieFound;
	
	// Buttons
	private Button backButton;
	private Button saveButton;
	
	// Editable Fields
	private EditText titleText;
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
        
        // Back button returns to main screen in default state
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
            }
			
		});
        
        // Save button currently changes the entry based on the currently set fields
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveMovieInfo();
            }
		});
    }
    
    /**
     * Read in all info into the GUI via the movie that was clicked on for this.
     */
    private void setMovieInfo(){
    	String list;
    	
    	// Modify top text fields
    	titleText = (EditText)findViewById(R.id.titleText);
    	titleText.setText(movieFound.getTitle() + " (" + movieFound.getYear() + ")");
    	
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
     * Save all info store in the current text fields.
     */
    private void saveMovieInfo(){
    	// Extract title and year
    	System.out.println(titleText.getText().toString().split("\\(")[0]);
    	String title = titleText.getText().toString().split("\\(")[0].trim();
    	String yearS = titleText.getText().toString().split("\\(")[1].replaceAll("\\)","").replaceAll(" ","");
    	int year = -1;
    	try {
    		year = Integer.parseInt(yearS);
    	}
    	catch (NumberFormatException e){
    		e.printStackTrace(System.out);
    	}
    	
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
    	movieFound.setDirector(directorText.getText() + "");
    	movieFound.setTitle(title);
    	movieFound.setYear(year);
    	movieFound.setCertification(certificationText.getText() + "");
    	movieFound.setLength(lengthText.getText() + "");
    	movieFound.setActors(actorsL);
    	movieFound.setGenre(genresL);
    	movieFound.setRating((int) ratingBar.getRating());
    }
}