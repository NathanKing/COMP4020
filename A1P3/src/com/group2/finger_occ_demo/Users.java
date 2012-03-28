package com.group2.finger_occ_demo;

import java.util.ArrayList;

/**
 * Manages the users in the application.
 */
public class Users {
	private ArrayList<User> users;
	private User curUser;
	
	public Users(){
		users = new ArrayList<User>();
		curUser = null;
	}
	
	public ArrayList<User> getUsers(){
		return users;
	}
	
	/**
	 * Adds or identifies user. In either case sets the current
	 * user. Send in the logging in variables, they are both null
	 * or empty the current user is logged out. (Not a user feature for testing)
	 */
	public void loginUser(String userName, String password){
		if ((userName == null || userName == "") && (password == null || password == ""))
			logoutUser();
		else{
			// set the user with the username if they exist
			boolean foundUser = false;
			for (User user : users){
				if (user.getUserName().equals(userName)){
					curUser = user;
					foundUser = true;
					break;
				}
			}
			
			//Otherwise add and set the user
			if (foundUser == false){
				users.add(new User(userName, password));
				curUser = users.get(users.size() - 1);
			}
		}
	}
	
	/**
	 * Sets the current user to nothing.
	 */
	public void logoutUser(){
		curUser = null;
	}
	
	public User currentUser(){
		return curUser;
	}
}
