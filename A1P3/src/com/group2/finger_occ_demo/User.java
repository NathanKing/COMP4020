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
		favourite = favourite.trim();
		if (!favourites.contains(favourite))
			favourites.add(favourite);
	}
	
	public void removeFavourite(String favourite){
		favourites.remove(favourite.trim());
	}
	
	// JF Recoded, added awesome.
	public void addFriend(String friend){
		if (!friends.contains(friend))
			friends.add(friend);
	}
	
	/**
	 * Adds to a movie if its name exists (and its not deleted) otherwise creates a new one.
	 */
	public void addMovie(Movie movieIn){
		boolean foundMovie = false;
		boolean isDeleted = false;
		int count = 0;
		for (Movie movie : movies){
			if (movie.getTitle().trim().equalsIgnoreCase(movieIn.getTitle().trim())){
				if (movie.isDeleted())
					isDeleted = true;
				foundMovie = true;
				break;
			}
			
			count += 1;
		}
		
		//remove movie so it can be added later
		if (isDeleted == true){
			movies.remove(count);
			count -= 1;
		}
		
		if (foundMovie == false)
			movies.add(movieIn);
		else
			movies.set(count, movieIn);
	}
	
	/**
	 * Gets the movie with the title. If no movie exists with the title then null is
	 * returned. Deleted movies are returned.
	 * Note: Due to Jackson JSON cannot be named getMovie().
	 */
	public Movie tryGetMovie(String title){
		title = title.trim();
		for (Movie movie : movies){
			if(movie.getTitle().trim().equalsIgnoreCase(title))
				return movie;
		}
		
		return null;
	}
	
	/**
	 * Try to delete the given movie from the users list. If successful also delete
	 * any corresponding entries in the favourites list. Note the movie will still
	 * exist in the main list, the user will just be unable to retrieve it.
	 */
	public void deleteMovie(String title){
		Movie movie = tryGetMovie(title);
		if (movie != null){
			movie.setDeleted(true);
			removeFavourite(title);
		}
		// create a movie entry that specifies deleted
		else{
			movie = new Movie();
			movie.setTitle(title);
			movie.setDeleted(true);
			movies.add(movie);
			removeFavourite(title);
		}
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
	
	public void setMovies(ArrayList<Movie> movies) {
		this.movies = movies ;
	}
}
