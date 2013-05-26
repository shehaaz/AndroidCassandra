package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;
import com.android.cassandra.droidbargain.feed.FeedAdapter;
import com.android.cassandra.droidbargain.feed.DealFactory;

public class StoreDealsFragment extends ListFragment {
	
	private StoreFactory store;
	private ArrayList<StoreFactory> store_data;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.activity_feed,container, false);
		
		store_data = FeedActivity.store_data;
		int store_index = (int) getActivity().getIntent().getIntExtra("THE_STORE_INDEX", 0);
		store = store_data.get(store_index);
		
		return rootView;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		ArrayList<DealFactory> store_deals = store.getDeal_data();
		Collections.sort(store_deals);
		FeedAdapter adapter =  new FeedAdapter(getActivity(), R.layout.feed_item, store_deals);
		setListAdapter(adapter);
		setRetainInstance(true);
	}

}
