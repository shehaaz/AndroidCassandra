package com.android.cassandra.droidbargain.stores;

public class StoreFactory {
	
	private String storeTitle;

	public StoreFactory(String string) {
		
		storeTitle = string;
	}

	public CharSequence getStoreTitle() {
		return storeTitle;
	}

}
