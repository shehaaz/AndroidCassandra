package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import com.android.cassandra.droidbargain.feed.FeedFactory;

public class StoreFactory {

	private String storeTitle;
	private String storeID;
	private String address;
	private double lat;
	private double lng;
	ArrayList<FeedFactory> deal_data = new ArrayList<FeedFactory>();



	public StoreFactory(String pStoreID,String pStoreTitle,String pAddress, double pLat, double pLng) {

		storeTitle = pStoreTitle;
		storeID = pStoreID;
		address = pAddress;
		lat = pLat;
		lng = pLng;
	}

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

	public void setDeal_data(FeedFactory pDeal) {
		deal_data.add(pDeal);
	}	

	public ArrayList<FeedFactory> getDeal_data(){
		return deal_data;
	}

	public String toString(){
		return storeTitle + " located at " + address;
	}

}
