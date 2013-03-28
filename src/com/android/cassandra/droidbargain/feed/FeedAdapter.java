package com.android.cassandra.droidbargain.feed;

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
	FeedFactory feedFactory[]=null;

	public FeedAdapter(Context context, int textViewResourceId,
			FeedFactory[] feedFactory) {
		super(context, textViewResourceId, feedFactory);
		
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.feedFactory = feedFactory;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		WireHolder holder = null;

		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new WireHolder();
			holder.dataTitle = (TextView) row.findViewById(R.id.feed_data_title);
			holder.personName = (TextView) row.findViewById(R.id.person_feed_name);

			row.setTag(holder);
		}
		else{
			holder = (WireHolder)row.getTag();
		}
		//For every item in the list. set Title, address and rating
		FeedFactory feed = feedFactory[position];
		holder.personName.setText(feed.getUser());
		holder.dataTitle.setText(feed.getData());

		return row;
	}

	static class WireHolder{
		TextView dataTitle;
		TextView personName;
	}

}
