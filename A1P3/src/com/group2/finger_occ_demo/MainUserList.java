package com.group2.finger_occ_demo;
import java.util.ArrayList;

import com.group2.finger_occ_demo.activities.FavouritesActivity;
import com.group2.finger_occ_demo.activities.FriendsListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainUserList extends Activity {
	User currUser;
	Button backButton;
	Button logoutButton;
	TextView userNameTextView;
	ListView mainList;
	String[] list;

	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.userlayout);
	        
	        userNameTextView = (TextView)findViewById(R.id.UserNameTextView);
	        mainList = (ListView)findViewById(R.id.listView1);
	        
	        Bundle b = this.getIntent().getExtras();
	        
	        if(b != null)
	        	currUser = (User)b.getSerializable("User");
	           
	
        	userNameTextView.setText((CharSequence)currUser.getUserName());
        	
        	list = GetMainList();
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        	mainList.setAdapter(adapter);
	        
        	backButton = (Button)findViewById(R.id.BackButtonMain);
        	System.out.println("backButton is: " + backButton);
        	backButton.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		finish();
            	}
            });
        	
        	// When logout button is clicked set current user to null and return to sign in/login screen.
        	logoutButton = (Button)findViewById(R.id.LogoutButton);
        	logoutButton.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		canvasApp.users.logoutUser();
            		finish();
            	}
            });
	        
	        mainList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String itemSelected = list[arg2];
					
					if(itemSelected == "Friends")
					{
						Bundle b = new Bundle();
						b.putSerializable("User", currUser);
						
						Intent myIntent = new Intent(arg1.getContext(), FriendsListActivity.class);
						myIntent.putExtras(b);
						
		                startActivityForResult(myIntent, 0);
					}
					else
					if(itemSelected == "Favorites")
					{
						Bundle b = new Bundle();
						b.putSerializable("User", currUser);
						
						Intent myIntent = new Intent(arg1.getContext(), FavouritesActivity.class);
						myIntent.putExtras(b);
						
		                startActivityForResult(myIntent, 0);
					}
					else
					{
						Bundle b = new Bundle();
						b.putSerializable("User", currUser);
						b.putSerializable("ListName", itemSelected);
						
						Intent myIntent = new Intent(arg1.getContext(), ListActivity.class);
						myIntent.putExtras(b);
						
		                startActivityForResult(myIntent, 0);
					}
				}
	        });
	 }
	 
	 private String[] GetMainList(){
		 ArrayList<String> list = currUser.makeTitleOfLists();
		 
		 String[] array = new String[list.size()];
		 
		 for(int i = 0; i < list.size(); i++)
		 {
			 array[i] = list.get(i);
		 }
		 
		 return array;
	 }
}
