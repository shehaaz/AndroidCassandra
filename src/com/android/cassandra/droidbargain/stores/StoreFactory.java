package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.cassandra.droidbargain.feed.FeedFactory;

public class StoreFactory implements Parcelable {

	private String storeID;
	private String storeTitle;
	private String address;
	private double lat;
	private double lng;
	ArrayList<FeedFactory> deal_data = new ArrayList<FeedFactory>();



	public StoreFactory(String pStoreID,String pStoreTitle,String pAddress, double pLat, double pLng) {

		storeID = pStoreID;
		storeTitle = pStoreTitle;
		address = pAddress;
		lat = pLat;
		lng = pLng;
	}
	
	public StoreFactory(Parcel source){
		storeID = source.readString();
		storeTitle = source.readString();
		address = source.readString();
		lat = source.readDouble();
		lng = source.readDouble();
		source.readTypedList(deal_data, FeedFactory.CREATOR);	
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeString(storeID);
		dest.writeString(storeTitle);
		dest.writeString(address);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeTypedList(deal_data);
	}
	
	public static final Creator<StoreFactory> CREATOR = new Creator<StoreFactory>() {
		
		@Override
		public StoreFactory createFromParcel(Parcel source) {
			return new StoreFactory(source);
		}
		
		@Override
		public StoreFactory[] newArray(int size){
			return new StoreFactory[size];
		}
	};

	public String getStoreTitle() {
		return storeTitle;
	}

	public String getStoreID() {
		return storeID;
	}

	public String getAddress() {
		return address;
	}

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}	

	public void addDeal(FeedFactory pDeal) {
		deal_data.add(pDeal);
	}	

	public ArrayList<FeedFactory> getDeal_data(){
		return deal_data;
	}

	public String toString(){
		return storeTitle + " located at " + address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
