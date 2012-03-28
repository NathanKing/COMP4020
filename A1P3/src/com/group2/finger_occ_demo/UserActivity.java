package com.group2.finger_occ_demo;

import com.group2.finger_occ_demo.data.Movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends Activity {
    /** Called when the activity is first created. */
	Button logInButton;
	EditText userNameTextBox;
	Button signUpButton;
	EditText passwordTextBox;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//If user is logged in don't present login screen
    	super.onCreate(savedInstanceState);
    	if (canvasApp.users.currentUser() == null){
	        setContentView(R.layout.loginlayout);
	        
	        //Test users
			System.out.println("---Users are---");
			for (User user : canvasApp.users.getUsers()){
				System.out.println(user.getUserName() + "---:");
				for (Movie movie : user.getMovies())
					System.out.println(movie.getTitle());
			}
	        
	        logInButton = (Button)findViewById(R.id.LogInButton);
	        signUpButton = (Button)findViewById(R.id.SignUpButton);
	        
	        userNameTextBox = (EditText)findViewById(R.id.UserNameTextBox);
	        passwordTextBox = (EditText)findViewById(R.id.PasswordTextBox);
	        
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