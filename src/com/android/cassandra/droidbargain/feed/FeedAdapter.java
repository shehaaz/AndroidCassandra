package com.android.cassandra.droidbargain.feed;

import java.util.ArrayList;

import com.android.cassandra.droidbargain.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedAdapter extends ArrayAdapter<FeedFactory>{
	
	Context context;
	int layoutResourceId;
	ArrayList<FeedFactory> feedFactory;

	public FeedAdapter(Context context, int textViewResourceId,
			ArrayList<FeedFactory> feed_data) {
		super(context, textViewResourceId, feed_data);
		
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.feedFactory = feed_data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		FeedHolder holder = null;

		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FeedHolder();
			holder.title = (TextView) row.findViewById(R.id.feed_title);
			holder.desc = (TextView) row.findViewById(R.id.feed_desc);
			holder.price = (TextView) row.findViewById(R.id.feed_price);
			holder.location = (TextView) row.findViewById(R.id.feed_location);
			holder.user = (TextView) row.findViewById(R.id.feed_posted_by);

			row.setTag(holder);
		}
		else{
			holder = (FeedHolder)row.getTag();
		}
		//For every item in the list. set Title, address and rating
		FeedFactory feed = feedFactory.get(position);
		holder.title.setText(feed.getTitle());
		holder.desc.setText(feed.getDesc());
		holder.price.setText(feed.getPrice()+"$");
		holder.location.setText(feed.getLocation());
		holder.user.setText("Posted By: "+feed.getUsername());

		return row;
	}

	static class FeedHolder{
		TextView title;
		TextView desc;
		TextView price;
		TextView location;
		TextView user;
	}

}
