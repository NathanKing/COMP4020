package com.group2.finger_occ_demo;

import java.io.Serializable;
import java.util.ArrayList;

public class MyList implements Serializable {
	private static final long serialVersionUID = 2531675824028552270L;
	private String title;
	@SuppressWarnings("unused")
	private Object type;
	private ArrayList<String> list;
	
	public MyList(String title, Object type)
	{
		this.title = title;
		this.type = type;
		list = new ArrayList<String>();
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void removeFromList(String item)
	{
		for(int i = 0; i < list.size();i++)
		{
			if(item.equals(list.get(i)))
			{
				list.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<String> getList()
	{
		return list;
	}
	
	public void addToList(String item)
	{
		boolean dupe = false;
		
		for(int i = 0; i < list.size(); i++)
		{
			if(item.equals(list.get(i)))
			{
				dupe = true;
				break;
			}
		}
		
		if(!dupe)
			list.add(item);
	}
}
