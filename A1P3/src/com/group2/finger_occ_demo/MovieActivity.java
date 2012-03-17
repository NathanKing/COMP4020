package com.group2.finger_occ_demo;

import com.group2.finger_occ_demo.data.Movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RatingBar;

public class MovieActivity extends Activity {
    /** Called when the activity is first created. */

	Button backButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Set up layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielayout);

        // Set the info
        setMovieInfo(MyCanvas.movieFound);
        
        // Back button returns to main screen in default state
        backButton = (Button)findViewById(R.id.SignUpButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
            }
			
		});
    }
    
    /**
     * Read in all info sent in by Movie.
     */
    private void setMovieInfo(Movie movie){
    	String list;
    	
    	((TextView)findViewById(R.id.textView2)).setText(movie.getTitle() + " (" + movie.getYear() + ")");
    	((TextView)findViewById(R.id.TextView01)).setText(movie.getDirector());
    	((TextView)findViewById(R.id.textView4)).setText(movie.getCertification());
    	((TextView)findViewById(R.id.textView5)).setText(movie.getLength());
    	
    	list = "";
    	for (String actor : movie.getActors())
    		list += actor + ",";
    	((TextView)findViewById(R.id.TextView05)).setText(list.substring(0, list.length()-1));
    			
		list = "";
    	for (String genre : movie.getGenre())
    		list += genre + ",";
    	((TextView)findViewById(R.id.TextView04)).setText(list.substring(0, list.length()-1));
    			
    	((RatingBar)findViewById(R.id.ratingBar1)).setRating(movie.getRating());
    }
}