package com.group2.finger_occ_demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.group2.finger_occ_demo.MainUserList;
import com.group2.finger_occ_demo.R;
import com.group2.finger_occ_demo.User;
import com.group2.finger_occ_demo.canvasApp;

public class UserActivity extends Activity {
    /** Called when the activity is first created. */
	Button logInButton;
	Button backButton;
	EditText userNameTextBox;
	EditText passwordTextBox;
	
	/**
	 * Handles any returns from screens generated from this screen.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// Automatically exit screen if login so can skip this
		if (canvasApp.users.currentUser() != null)
			finish();
		// If on a logout create the screen from scratch, note I have limited the use
		// of bundle here but it shouldn't matter.
		else
			onCreate(null);
    }
	
    public void onCreate(Bundle savedInstanceState) {
    	//If user is logged in don't present login screen
    	super.onCreate(savedInstanceState);
    	if (canvasApp.users.currentUser() == null){
	        setContentView(R.layout.loginlayout);
	        
	        logInButton = (Button)findViewById(R.id.LogInButton);
	        
	        userNameTextBox = (EditText)findViewById(R.id.UserNameTextBox);
	        passwordTextBox = (EditText)findViewById(R.id.PasswordTextBox);
	        
	        backButton = (Button)findViewById(R.id.BackButtonLogin);
        	backButton.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		finish();
            	}
            });
	        
	        /**
	         *  Going to just create a user in login instead of signup.
	         *  */
	        logInButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Generate the user and add to the list of users
					canvasApp.users.loginUser(userNameTextBox.getText() + "", passwordTextBox.getText() + "");
					User currUser = canvasApp.users.currentUser();
					
					// If the user was logged in, only case where this does not happen is logout which is implicit.
					if (currUser != null){
						//fake User stuff
						currUser.addFriend("Fake Friend 1");
						currUser.addFriend("Fake friend 2");
						
						Bundle b = new Bundle();
						
						b.putSerializable("User", currUser);
						
						Intent myIntent = new Intent(v.getContext(), MainUserList.class);
						myIntent.putExtras(b);
						
		                startActivityForResult(myIntent, 0);
					}
	            }
				
			});
    	}
    	else{
    		Bundle b = new Bundle();
			b.putSerializable("User", canvasApp.users.currentUser());

			Intent myIntent = new Intent(this, MainUserList.class);
			myIntent.putExtras(b);
			
            startActivityForResult(myIntent, 0);
    	}
    }
}