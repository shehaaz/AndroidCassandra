package com.android.cassandra.droidbargain.profile;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6340663410787613060L;
	
	private String name;
	private String uid;
	
	public User(String pName, String pUid){
		name = pName;
		uid = pUid;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUser_ID(){
		return uid;
	}
	
	public String getUserPhoto(){
		return "http://graph.facebook.com/"+uid+"/picture?type=large";
	}

	
	public String toString(){
		return "This is " + uid + " " + name;
	}
}
