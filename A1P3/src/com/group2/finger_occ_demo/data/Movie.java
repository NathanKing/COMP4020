package com.group2.finger_occ_demo.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores all the information about a movie including tile, director, actors etc.
 */
public class Movie implements Serializable{
	private static final long serialVersionUID = 1761136447996756052L;
	private String title;
	private int year;
	@SuppressWarnings("unused")
	private int year1900;// For Jackson JSONing
	private String length;
	private String certification;
	private String director;
	private int rating;
	private List<String> genre = new ArrayList<String>();
	private List<String> actors = new ArrayList<String>();
	private String actor;
	
	// Need dummy constructor for Jackson
	public Movie(){}
	
	public Movie(String title, int year, String length, String certification,
			String director, int rating, ArrayList<String> genre,
			ArrayList<String> actors, String actor) {
		super();
		this.title = title;
		this.year = year;
		this.length = length;
		this.certification = certification;
		this.director = director;
		this.rating = rating;
		this.genre = genre;
		this.actors = actors;
		this.actor = actor;
	}
	
	/*
	 * Getters and Setters
	 * */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	/**
	 * get year from 1900
	 */
	public int getYear1900() {
		return year - 1900;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public List<String> getActors() {
		if (actors == null)
			actors = new ArrayList<String>();
		
		if (actor != null){
			actors.add(actor);
			actor = null;
		}
		assert(actors != null);
		
		return actors;
	}
	public void setActors(ArrayList<String> actor) {
		this.actors = actor;
	}
	public List<String> getGenre() {
		return genre;
	}
	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}
}
