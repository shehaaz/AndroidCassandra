package com.android.cassandra.droidbargain;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6340663410787613060L;
	
	private String name;
	private String email;
	
	public User(String pName, String pEmail){
		name = pName;
		email = pEmail;
	}
	
	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}
	
	public String getAttName(){
		return "name";
	}
	
	public String getAttEmail(){
		return "email";
	}
	
	public String toString(){
		return "This is " + name + " " + email;
	}
}
