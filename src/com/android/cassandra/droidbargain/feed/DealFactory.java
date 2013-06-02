package com.android.cassandra.droidbargain.feed;

import android.os.Parcel;
import android.os.Parcelable;

public class DealFactory implements Parcelable, Comparable<DealFactory> {


	private String timeStamp;
	private String image;
	private String desc;
	private String price;
	private String location;
	private String user_name;
	private String store_id;
	private String user_id;


	public DealFactory(String timeStamp, String image, String desc,
			String price, String location, String user_name, String store_id, String user_id)
	{
		this.timeStamp = timeStamp;
		this.image = image;
		this.desc = desc;
		this.price = price;
		this.location = location;
		this.user_name = user_name;
		this.store_id = store_id;
		this.user_id = user_id;
	}

	public String getTimeStamp(){
		return timeStamp;
	}

	public String getImage(){
		return image;
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
	
	
	public String getStoreID(){
		return store_id;
	}
	
	public String getUserID(){
		return user_id;
	}

	public DealFactory(Parcel source){
		timeStamp = source.readString();
		image = source.readString();
		desc = source.readString();
		price = source.readString();
		location = source.readString();
		user_name = source.readString();
		store_id = source.readString();
		user_id = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(timeStamp);
		dest.writeString(image);
		dest.writeString(desc);
		dest.writeString(price);
		dest.writeString(location);	
		dest.writeString(user_name);
		dest.writeString(store_id);
		dest.writeString(user_id);
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
