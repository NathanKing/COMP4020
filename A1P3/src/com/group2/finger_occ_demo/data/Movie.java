package com.group2.finger_occ_demo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the information about a movie including tile, director, actors etc.
 */
public class Movie {
	private String title;
	private int year;
	private String length;
	private String certification;
	private String director;
	private int rating;
	private List<String> genre = new ArrayList<String>();
	private List<String> actors = new ArrayList<String>();
	private String actor;
	
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
		if (actor != null){
			actors.add(actor);
			actor = null;
		}
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
