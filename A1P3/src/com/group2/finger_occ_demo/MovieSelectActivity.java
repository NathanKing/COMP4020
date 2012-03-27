package com.group2.finger_occ_demo;

import java.util.ArrayList;

import com.group2.finger_occ_demo.data.Movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RatingBar;

public class MovieSelectActivity extends Activity implements OnItemClickListener{
    /** Called when the activity is first created. */
	
	ListView moviesList;
	ArrayAdapter<String> adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Set up layout
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.movieselectlayout);
        // Set the info
       
        ArrayList<String>movies = new ArrayList<String>();
        
        for(Movie m:MyCanvas.moviesFound){
        	movies.add(m.getTitle());
        }
        moviesList = (ListView)findViewById(R.id.movielist);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, movies);
    	moviesList.setAdapter(adapter);
        
    	moviesList.setOnItemClickListener(this);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		System.out.println("item clicked");
		
		this.setResult(position);
		
		this.finish();
	}
    
    
}