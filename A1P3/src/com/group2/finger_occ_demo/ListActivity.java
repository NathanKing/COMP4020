package com.group2.finger_occ_demo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends Activity{
	Button backButton;
	Button shareButton;
	ListView list;
	ArrayAdapter<String> adapter;
	User currUser;
	TextView listNameTextView;
	String listName;
	 
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);
        
        backButton = (Button)findViewById(R.id.BackButton);
        shareButton = (Button)findViewById(R.id.ShareButton);
        list  = (ListView)findViewById(R.id.listView1);
        listNameTextView = (TextView)findViewById(R.id.listNameTextView);
        
        Bundle b = this.getIntent().getExtras();
        
        currUser = (User)b.getSerializable("User");
        listName = (String)b.getSerializable("ListName");
        
        adapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, currUser.getList(listName));
        list.setAdapter(adapter);
        
        listNameTextView.setText((CharSequence) listName);
        
        backButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		finish();
        		Bundle b = new Bundle();
				b.putSerializable("User", currUser);
				
				Intent myIntent = new Intent(v.getContext(), MainUserList.class);
				myIntent.putExtras(b);
				
                startActivityForResult(myIntent, 0);
        	}
        });
        
        shareButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity.this);
        		
        		alert.setTitle("Share");
        		alert.setMessage("Share with a Friend");
        		
        		final ListView input = new ListView(ListActivity.this);
        		alert.setView(input);
        		
        		ArrayAdapter<String> friendadapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, currUser.getFriends());
        	    input.setAdapter(friendadapter);
        		
        		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        				//Send list to friend
        				
        			  }
        			});

        			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        			  public void onClick(DialogInterface dialog, int whichButton) {
        			    // Canceled.
        			  }
        			});

        			alert.show();
        	}
        });
	
	}
}
