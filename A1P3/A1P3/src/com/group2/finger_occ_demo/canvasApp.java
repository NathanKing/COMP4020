package com.group2.finger_occ_demo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.group2.finger_occ_demo.data.DataObjects;
import com.group2.finger_occ_demo.data.Movie;

/**
 * Initializes Canvas and starts application.
 */
public class canvasApp extends Activity implements OnItemSelectedListener {
	
	public static DataObjects data;
	public static List<String> directors = new ArrayList<String>();
	//Application Constants
	private final String FILE_NAME = "movies.json";
	
	private MyCanvas canvasView;
	
    /** Called when the activity is first created.. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DataObjectCreator creator = new DataObjectCreator(this.getAssets(), getBaseContext());
    	
        super.onCreate(savedInstanceState);
        
        // Give default view until main screen is loaded
        setContentView(R.layout.main);
        
        // Get the data from the JSON file
        data = creator.createObjects(FILE_NAME);

        //create directors list
        for(Movie m:data.getMovie()){
        	
        	if(!(directors.contains(m.getDirector()))){
        		directors.add(m.getDirector());
        	}
        }
        
        directors.add(0, "All");
        Collections.sort(directors);
        ArrayAdapter<CharSequence> adapter;
        
        Spinner dirs = (Spinner) findViewById(R.id.directors);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, directors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dirs.setAdapter(adapter);

        Spinner genres = (Spinner) findViewById(R.id.genres);
        adapter = ArrayAdapter.createFromResource(this, R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genres.setAdapter(adapter);
        
        Spinner ratings = (Spinner) findViewById(R.id.ratings);
        adapter = ArrayAdapter.createFromResource(this, R.array.rating_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratings.setAdapter(adapter);
        
        
        // Create a new canvas set it as the view and give it focus. Also give it
        // the data created from the json file.
		canvasView = new MyCanvas(this);
		canvasView.setBackgroundColor(Color.WHITE);
		
		dirs.setOnItemSelectedListener(this);
		genres.setOnItemSelectedListener(this);
		ratings.setOnItemSelectedListener(this);
		
		FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
		
		frame.addView(canvasView);
    }
    
    @Override
    public void onBackPressed(){
    	ObjectMapper mapper = new ObjectMapper();//For Jackson JSON
        FileOutputStream testFile = null;
        
		try {
			testFile = getBaseContext().openFileOutput(FILE_NAME, Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        
        try {
			mapper.writeValue(testFile, data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	this.finish();
    }
    
    @Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		 		 
		Spinner genres = (Spinner)findViewById(R.id.genres);
		Spinner dirs = (Spinner)findViewById(R.id.directors);
		Spinner ratings = (Spinner)findViewById(R.id.ratings);
		
		String genre = genres.getSelectedItem().toString();
		String rating = ratings.getSelectedItem().toString();
		
		if(parent.getId() == R.id.genres){
			 
			genre = genres.getSelectedItem().toString();
		}
		
		else if(parent.getId() == R.id.ratings){
			 
			rating = ratings.getSelectedItem().toString();
		}
	
	 	canvasView.scatterView.points.filter_points(genre, rating);

	 	canvasView.invalidate();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	
	
	public void login(View view){
		
		Intent intent = new Intent(this, UserActivity.class);
		
		startActivityForResult(intent, 0);
		
		
		
	}
    
}