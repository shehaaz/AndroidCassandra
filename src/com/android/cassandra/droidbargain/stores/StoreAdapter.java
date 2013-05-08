package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedFactory;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class StoreAdapter extends ArrayAdapter<StoreFactory>{
	
	Context context;
	int layoutResourceId;
	ArrayList<StoreFactory> storeFactory;

	public StoreAdapter(Context context, int textViewResourceId,
			ArrayList<StoreFactory> storeFactory) {
		super(context, textViewResourceId, storeFactory);
		
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.storeFactory = storeFactory;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		StoreHolder holder = null;

		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new StoreHolder();
			holder.storeTitle = (TextView) row.findViewById(R.id.title_store);

			row.setTag(holder);
		}
		else{
			holder = (StoreHolder)row.getTag();
		}
		//For every item in the list. set Title, address and rating
		StoreFactory feed = storeFactory.get(position);
		holder.storeTitle.setText(feed.getStoreTitle());

		return row;
	}

	static class StoreHolder{
		TextView storeTitle;
	}
}
