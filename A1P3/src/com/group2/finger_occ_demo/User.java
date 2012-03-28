package com.group2.finger_occ_demo;

import java.io.Serializable;
import java.util.ArrayList;

import com.group2.finger_occ_demo.data.Movie;


public class User implements Serializable{
	private static final long serialVersionUID = -5403068240076986048L;
	private String userName;
	private String password;
	private ArrayList<String> friends;
	private ArrayList<String> favourites;
	
	// This will store any user customized movies
	private ArrayList<Movie> movies;
	
	// Needed for Jackson JSON
	public User(){}
	
	public User(String n, String p)
	{
		userName = n;
		password = p;
		friends = new ArrayList<String>();
		favourites = new ArrayList<String>();
		movies = new ArrayList<Movie>();
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	// Needed for Jackson JSON
	public String getPassword(){
		return password;
	}
	
	public void addFavourite(String favourite){
		if (!favourites.contains(favourite))
			favourites.add(favourite);
	}
	
	// JF Recoded, added awesome.
	public void addFriend(String friend){
		if (!friends.contains(friend))
			friends.add(friend);
	}
	
	/**
	 * Adds to a movie if its name exists otherwise creates a new one.
	 */
	public void addMovie(Movie movieIn){
		boolean foundMovie = false;
		int count = 0;
		for (Movie movie : movies){
			if(movie.getTitle().equalsIgnoreCase(movieIn.getTitle())){
				foundMovie = true;
				break;
			}
			count++;
		}
		
		
		if (foundMovie == false)
			movies.add(movieIn);
		else
			movies.set(count, movieIn);
	}
	
	/**
	 * Gets the movie with the title. If no movie exists with the title then null
	 * is returned.
	 * Note: Due to Jackson JSON cannot be named getMovie().
	 */
	public Movie tryGetMovie(String title){
		for (Movie movie : movies){
			if(movie.getTitle().equalsIgnoreCase(title.trim()))
				return movie;
		}
		
		return null;
	}
	
	public boolean matchPW(String p){
		if(password.equals(p))
			return true;
		return false;
	}
	
	public ArrayList<String> makeTitleOfLists(){
		ArrayList<String> titles = new ArrayList<String>();
		titles.add("Friends");
		titles.add("Favorites");

		return titles;
	}
	
	public ArrayList<String> getFavourites(){
		return favourites;
	}
	
	public ArrayList<String> getFriends(){
		return friends;
	}

	public ArrayList<Movie> getMovies() {
		return movies;
	}
}
