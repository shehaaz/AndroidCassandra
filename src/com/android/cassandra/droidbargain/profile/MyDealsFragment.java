package com.android.cassandra.droidbargain.profile;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedAdapter;
import com.android.cassandra.droidbargain.feed.FeedFactory;

public class MyDealsFragment extends ListFragment {

	ArrayList<FeedFactory> myDeals;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.activity_feed,container, false);
		myDeals = Profile.user_deal_data;
		
		if(myDeals != null){
			Collections.sort(myDeals);
		}

		return rootView;
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		FeedAdapter adapter =  new FeedAdapter(getActivity(), R.layout.feed_item, myDeals);
		setListAdapter(adapter);
		setRetainInstance(true);
	}

}
