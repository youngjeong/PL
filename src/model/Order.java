package model;

import java.io.Serializable;

public class Order implements Serializable
{
	private String date;
	private String menu;

	public Order(String date, String menu)
	{
		this.date = date;
		this.menu = menu;
	}

	public String getDate()
	{
		return date;
	}

	public String getMenu()
	{
		return menu;
	}
}
