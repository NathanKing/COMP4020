package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class FavouritesActivity extends Activity{
	Button backButton;
	ListView favoritesList;
	User currUser;
	ArrayAdapter<String> adapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingslayout);
        
        backButton = (Button)findViewById(R.id.BackButton);
        favoritesList = (ListView)findViewById(R.id.ratingsView);
        
        currUser = canvasApp.users.currentUser();
    	
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, currUser.getFavourites());
    	favoritesList.setAdapter(adapter);
        
        backButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		finish();
        	}
        });
        
        /**
         * Opens the current movie view clicked on.
         */
        favoritesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String title = currUser.getFavourites().get(arg2);
				
				// Search users modified movies then all the movies for the favorite movie clicked on
				MyCanvas.movieFound = currUser.tryGetMovie(title);
				if(MyCanvas.movieFound == null)
					MyCanvas.movieFound = canvasApp.data.tryGetMovie(title);
				
				// Open the movie screen
    			startActivityForResult(new Intent(arg1.getContext(), MovieActivity.class), MyCanvas.MOVIE_VIEW_PROCESS);
			}
        });
	}
}
