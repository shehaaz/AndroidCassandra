package com.android.cassandra.droidbargain.feed;

import android.os.Parcel;
import android.os.Parcelable;

public class DealFactory implements Parcelable, Comparable<DealFactory> {


	private String timeStamp;
	private String title;
	private String desc;
	private String price;
	private String location;
	private String user_name;


	public DealFactory(String timeStamp, String title, String desc,
			String price, String location, String user_name)
	{
		this.timeStamp = timeStamp;
		this.title = title;
		this.desc = desc;
		this.price = price;
		this.location = location;
		this.user_name = user_name;
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
	
	public String getUsername(){
		return user_name;
	}

	public DealFactory(Parcel source){
		timeStamp = source.readString();
		title = source.readString();
		desc = source.readString();
		price = source.readString();
		location = source.readString();
		user_name = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(timeStamp);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeString(price);
		dest.writeString(location);	
		dest.writeString(user_name);
	}

	public static final Creator<DealFactory> CREATOR = new Creator<DealFactory>() {

		public DealFactory createFromParcel(Parcel source) {
			return new DealFactory(source);
		}

		public DealFactory[] newArray(int size) {
			return new DealFactory[size];
		}
	};


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compareTo(DealFactory another) {
		Double compareQuantity = Double.parseDouble(((DealFactory) another).getTimeStamp()); 
		 
		//descending order
		return (int) (compareQuantity -  Double.parseDouble(this.getTimeStamp()));
	}
}
