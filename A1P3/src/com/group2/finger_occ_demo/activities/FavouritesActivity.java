package com.group2.finger_occ_demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.group2.finger_occ_demo.MyCanvas;
import com.group2.finger_occ_demo.R;
import com.group2.finger_occ_demo.User;
import com.group2.finger_occ_demo.canvasApp;

public class FavouritesActivity extends Activity{
	Button backButton;
	ListView favoritesList;
	User currUser;
	ArrayAdapter<String> adapter;
	
	/**
	 * Handles any returns from screens generated from this screen. Like viewing a movie.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		//Make sure list is updated
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, currUser.getFavourites());
    	favoritesList.setAdapter(adapter);
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingslayout);//
        
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
