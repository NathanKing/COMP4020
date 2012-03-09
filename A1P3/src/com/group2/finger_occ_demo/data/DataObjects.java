package com.group2.finger_occ_demo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the data points follows the same structure as the
 * JSON file.
 */
public class DataObjects {
	private List<Movie> movies = new ArrayList<Movie>();

	/*
	 * Getters and Setters
	 */
	public List<Movie> getMovie() {
		return movies;
	}
}
