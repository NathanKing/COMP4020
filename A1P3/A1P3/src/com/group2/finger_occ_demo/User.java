package com.group2.finger_occ_demo;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable{
	private static final long serialVersionUID = -5403068240076986048L;
	private String name;
	private String pw;
	private ArrayList<String> friends;
	private ArrayList<MyList> lists;
	private ArrayList<Rating> ratings;
	
	public User(String n, String p)
	{
		name = n;
		pw = p;
		friends = new ArrayList<String>();
		lists = new ArrayList<MyList>();
		ratings = new ArrayList<Rating>();
	}
	
	public ArrayList<Rating> getRatings()
	{
		return ratings;
	}
	
	public String getUserName()
	{
		return name;
	}
	
	public ArrayList<String> getList(String name)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < lists.size(); i++)
		{
			if(lists.get(i).getTitle().equals(name))
			{
				list = lists.get(i).getList();
				break; 
			}
		}
		
		return list;
	}
	
	public void addRating(Rating rating)
	{
		//check if we've already rated this movie. If so then just update the existing score.
		for(int i = 0; i < ratings.size(); i++)
		{
			if(ratings.get(i).getTitle().equals(rating.getTitle()))
			{
				ratings.get(i).setScore(rating.getScore());
				break;
			}
		}
		
		ratings.add(rating);
	}
	
	public void addFriend(String friend)
	{
		boolean dupe = false;
		//Check if user is already friends with that user
		for(int i = 0; i < friends.size(); i++)
		{
			//Don't add duplicate friends
			if(friends.get(i).equals(friend))
			{
				dupe = true;
				break;
			}
		}
		
		if(!dupe)
		{
			friends.add(friend);
		}
	}
	
	public boolean matchPW(String p)
	{
		if(pw.equals(p))
			return true;
		else
			return false;
	}
	
	public void addList(MyList list)
	{
		lists.add(list);
	}
	
	public ArrayList<String> getTitleOfLists()
	{
		ArrayList<String> titles = new ArrayList<String>();
		
		titles.add("Friends");
		titles.add("My Ratings");
		
		for(int i = 0; i < lists.size(); i++)
		{
			titles.add(lists.get(i).getTitle());
		}
		

		return titles;
	}
	
	public ArrayList<String> getFriends()
	{
		return friends;
	}
}
