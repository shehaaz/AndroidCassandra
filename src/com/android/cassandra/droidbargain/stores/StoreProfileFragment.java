package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;

public class StoreProfileFragment extends Fragment {

	private StoreFactory store;
	private ArrayList<StoreFactory> store_data;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.store_profile,container, false);

		store_data = FeedActivity.store_data;
		int store_index = (int) getActivity().getIntent().getIntExtra("THE_STORE_INDEX", 0);
		store = store_data.get(store_index);

		TextView store_title = (TextView) rootView.findViewById(R.id.store_title);

		store_title.setText(store.getStoreTitle());

		TextView store_address = (TextView) rootView.findViewById(R.id.store_location);

		store_address.setText(store.getAddress());

		TextView store_num_posts = (TextView) rootView.findViewById(R.id.store_num_deal);

		int num_deals = store.getDeal_data().size();

		switch(num_deals){

		case 0: 
			store_num_posts.setText("No Deals Yet!");
			break;
		case 1:
			store_num_posts.setText(num_deals + " Deal Right Now!");
			break;
		default:
			store_num_posts.setText(num_deals + " Deals Right Now!");
		}

		return rootView;

	}

}
