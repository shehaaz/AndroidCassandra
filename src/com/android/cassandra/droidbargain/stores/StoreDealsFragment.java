package com.android.cassandra.droidbargain.stores;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedAdapter;

public class StoreDealsFragment extends ListFragment {
	
	private StoreFactory store;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.activity_feed,container, false);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		store = (StoreFactory)bundle.getParcelable("THE_STORE");
		
		return rootView;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		FeedAdapter adapter =  new FeedAdapter(getActivity(), R.layout.feed_item, store.getDeal_data());
		setListAdapter(adapter);
		setRetainInstance(true);
	}

}
