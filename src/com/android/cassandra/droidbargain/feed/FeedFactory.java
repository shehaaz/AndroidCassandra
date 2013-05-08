package com.android.cassandra.droidbargain.feed;

public class FeedFactory {


	private String timeStamp;
	private String title;
	private String desc;
	private String price;
	private String location;
	

	public FeedFactory(String timeStamp, String title, String desc,
			String price, String location)
	{
		this.timeStamp = timeStamp;
		this.title = title;
		this.desc = desc;
		this.price = price;
		this.location = location;
	}

	public String getTimeStamp(){
		return timeStamp;
	}

	public String getTitle(){
		return title;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public String getPrice(){
		return price;
	}
	
	public String getLocation(){
		return location;
	}
}
