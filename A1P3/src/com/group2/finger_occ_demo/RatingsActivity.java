package com.group2.finger_occ_demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RatingsActivity extends Activity{
	Button backButton;
	ListView ratingsList;
	User currUser;
	SimpleAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingslayout);
        
        backButton = (Button)findViewById(R.id.BackButton);
        ratingsList = (ListView)findViewById(R.id.ratingsView);
        
        Bundle b = this.getIntent().getExtras();
        
        currUser = (User)b.getSerializable("User");
        
        ArrayList<Map<String, String>> list = buildData();
        String[] from = { "title", "rating" };
		int[] to = { android.R.id.text1, android.R.id.text2 };
        
        adapter = new SimpleAdapter(this,list,
				android.R.layout.simple_list_item_2, from, to);
    	ratingsList.setAdapter(adapter);
        
        
        backButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		finish();
        	}
        });
	}
	
	private ArrayList<Map<String, String>> buildData()
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ArrayList<Rating> ratings = currUser.getRatings();
		
		
		for(int i = 0; i < ratings.size(); i++)
		{
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("title", ratings.get(i).getTitle());
			item.put("rating", ratings.get(i).getScore() + "/5");
			list.add(item);
		}
		
		return list;
	}
}
