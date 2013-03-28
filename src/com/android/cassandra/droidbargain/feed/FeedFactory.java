package com.android.cassandra.droidbargain.feed;

public class FeedFactory {


	private String user;
	private String data;

	public FeedFactory(String pUser, String pData){
		user= pUser;
		data = pData;
	}

	public String getUser(){

		return user;

	}

	public String getData(){
		return data;
	}
}
