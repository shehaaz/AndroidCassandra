package com.android.cassandra.droidbargain.profile;

import java.util.ArrayList;
import java.util.Collections;

import com.android.cassandra.droidbargain.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.cassandra.droidbargain.feed.DealFactory;
import com.android.cassandra.droidbargain.feed.FeedAdapter;

public class MyFavDealsFragment extends ListFragment {

	ArrayList<DealFactory> myFavDeals;
	private User bargain_user;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.activity_feed,container, false);
		
		bargain_user = (User) getActivity().getIntent().getSerializableExtra("USER_PROFILE");
		
		myFavDeals = Profile.user_like_data;
		
		if(myFavDeals != null){
			Collections.sort(myFavDeals);
		}

		return rootView;
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		FeedAdapter adapter =  new FeedAdapter(getActivity(), R.layout.feed_item, myFavDeals, bargain_user.getUser_ID());
		setListAdapter(adapter);
		setRetainInstance(true);
	}

}
