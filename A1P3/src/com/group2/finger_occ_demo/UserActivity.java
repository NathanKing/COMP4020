package com.group2.finger_occ_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends Activity {
    /** Called when the activity is first created. */

	
	User currUser;
	
	Button logInButton;
	EditText userNameTextBox;
	Button signUpButton;
	EditText passwordTextBox;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        
        logInButton = (Button)findViewById(R.id.LogInButton);
        signUpButton = (Button)findViewById(R.id.SignUpButton);
        
        userNameTextBox = (EditText)findViewById(R.id.UserNameTextBox);
        passwordTextBox = (EditText)findViewById(R.id.PasswordTextBox);
        
        logInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currUser = new User(userNameTextBox.getText().toString(), passwordTextBox.getText().toString());
				
				//fake User stuff
				MyList afakeList = new MyList("Fake List", String.class);
				afakeList.addToList("test");
				currUser.addList(afakeList);
				currUser.addFriend("Fake Friend 1");
				currUser.addFriend("Fake friend 2");
				
				currUser.addRating(new Rating("Some Like it Hoth",4));
				//fake User Stuff
				
				
				Bundle b = new Bundle();
				
				b.putSerializable("User", currUser);
				
				Intent myIntent = new Intent(v.getContext(), MainUserList.class);
				myIntent.putExtras(b);
				
                startActivityForResult(myIntent, 0);
            }
			
		});
    }
}