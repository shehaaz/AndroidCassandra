package com.android.cassandra.droidbargain.stores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cassandra.droidbargain.R;

public class StoreProfileFragment extends Fragment {
	
	private StoreFactory store;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.store_profile,container, false);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		store = (StoreFactory)bundle.getParcelable("THE_STORE");
		
		TextView store_title = (TextView) rootView.findViewById(R.id.store_title);
		
		store_title.setText(store.getStoreTitle());
		
		
		
		return rootView;
		
		
	}

}
