package com.group2.finger_occ_demo.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the data point, follows the same structure as the JSON file. Contains methods to
 * query the data.
 */
public class DataObjects implements Serializable{
	private static final long serialVersionUID = -2568955704117037872L;
	private List<Movie> movies = new ArrayList<Movie>();
	private int latestYear;
	private int highestRating;
	
	public DataObjects() {
		latestYear = -1;
		highestRating = -1;
	}

	/*
	 * Getters and Setters
	 */
	public List<Movie> getMovie() {
		return movies;
	}
	
	/**
	 * Gets the latest year
	 */
	public int getLatestYear(){
		//cache this so other classes don't have to worry about caching
		if (latestYear < 0){
			for (Movie movie : movies){
				if (movie.getYear() > latestYear)
					latestYear = movie.getYear();
			}
		}
		
		return latestYear;
	}
	
	/**
	 * Gets the highest rating
	 */
	public int getHighestRating(){
		//cache this so other classes don't have to worry about caching
		if (highestRating < 0){
			for (Movie movie : movies){
				if (movie.getRating() > highestRating)
					highestRating = movie.getRating();
			}
		}
		
		return highestRating;
	}
}
