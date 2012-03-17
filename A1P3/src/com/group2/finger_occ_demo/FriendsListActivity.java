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

public class FriendsListActivity extends Activity {
	Button backButton;
	Button addButton;
	ListView friendsList;
	ArrayAdapter<String> adapter;
	User currUser;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslayout);
        
        backButton = (Button)findViewById(R.id.BackButton);
        addButton = (Button)findViewById(R.id.AddButton);
        friendsList = (ListView)findViewById(R.id.friendslist);
        
        Bundle b = this.getIntent().getExtras();
        
        currUser = (User)b.getSerializable("User");
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, currUser.getFriends());
    	friendsList.setAdapter(adapter);
        
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
        
        addButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		AlertDialog.Builder alert = new AlertDialog.Builder(FriendsListActivity.this);
        		
        		alert.setTitle("Add");
        		alert.setMessage("Add a Friend");
        		
        		final EditText input = new EditText(FriendsListActivity.this);
        		alert.setView(input);
        		
        		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        				String value = input.getText().toString();
        			  	currUser.addFriend(value);
              			adapter.notifyDataSetChanged();
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
