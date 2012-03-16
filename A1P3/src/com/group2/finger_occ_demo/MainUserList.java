package com.group2.finger_occ_demo;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainUserList extends Activity {
	User currUser;
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
	        
	        
	        
	        mainList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
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
					if(itemSelected == "My Ratings")
					{
						Bundle b = new Bundle();
						b.putSerializable("User", currUser);
						
						Intent myIntent = new Intent(arg1.getContext(), RatingsActivity.class);
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
	 
	 private String[] GetMainList()
	 {
		 ArrayList<String> list = currUser.getTitleOfLists();
		 
		 String[] array = new String[list.size()];
		 
		 for(int i = 0; i < list.size(); i++)
		 {
			 array[i] = list.get(i);
		 }
		 
		 return array;
	 }
}
