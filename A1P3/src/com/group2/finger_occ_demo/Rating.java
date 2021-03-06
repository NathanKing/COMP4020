package com.group2.finger_occ_demo;

import java.io.Serializable;

public class Rating implements Serializable {
	private static final long serialVersionUID = -581868627525928224L;
	private String title;
	private int score;
	
	// For Jackson JSON
	public Rating(){}
	
	public Rating(String title, int score)
	{
		this.title = title;
		this.score = score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public String getTitle()
	{
		return title;
	}
}
