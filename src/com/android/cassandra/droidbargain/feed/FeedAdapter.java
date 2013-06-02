package com.android.cassandra.droidbargain.feed;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cassandra.droidbargain.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FeedAdapter extends ArrayAdapter<DealFactory>{
	
	Context context;
	int layoutResourceId;
	ArrayList<DealFactory> feedFactory;
	private String timestamp;
	private Calendar calendar;

	public FeedAdapter(Context context, int textViewResourceId,
			ArrayList<DealFactory> feed_data) {
		super(context, textViewResourceId, feed_data);
		
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.feedFactory = feed_data;
		calendar = Calendar.getInstance();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		FeedHolder holder = null;
		
		DealFactory deal = feedFactory.get(position);
		
		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FeedHolder();
			holder.image = (ImageView) row.findViewById(R.id.feed_image);
			holder.desc = (TextView) row.findViewById(R.id.feed_desc);
			holder.price = (TextView) row.findViewById(R.id.feed_price);
			holder.location = (TextView) row.findViewById(R.id.feed_location);
			holder.user = (TextView) row.findViewById(R.id.feed_posted_by);
			holder.thumbs_up = (ImageButton) row.findViewById(R.id.thumbs_up);
						
			row.setTag(holder);
			holder.thumbs_up.setTag(holder);
		}
		else{
			holder = (FeedHolder)row.getTag();
		}
		
		byte [] output = Base64.decode(deal.getImage(), Base64.DEFAULT);
		Bitmap outputImage = BitmapFactory.decodeByteArray(output, 0, output.length);
		
		holder.image.setImageBitmap(outputImage);
		holder.desc.setText(deal.getDesc());
		holder.price.setText("Price: "+deal.getPrice());
		holder.location.setText(deal.getLocation());
		holder.user.setText("Posted By: "+deal.getUsername());
		
		holder.holder_timestamp = deal.getTimeStamp();
		holder.holder_image =  deal.getImage();
		holder.holder_desc = deal.getDesc();
		holder.holder_price = deal.getPrice();
		holder.holder_location = deal.getLocation();
		holder.holder_name = deal.getUsername();
		holder.store_id = deal.getStoreID();
		holder.user_id = deal.getUserID();
		
		
		
		holder.thumbs_up.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				
				FeedHolder tempHolder = (FeedHolder) v.getTag();
				
				Toast.makeText(context, "Fav'ed Post", Toast.LENGTH_SHORT).show();
 
				try {
					AsyncHttpClient client = new AsyncHttpClient();
					JSONObject jsonParams = new JSONObject();
					timestamp = String.valueOf(calendar.getTimeInMillis());
					jsonParams.put("body", tempHolder.holder_desc);
					jsonParams.put("price", tempHolder.holder_price);
					jsonParams.put("location", tempHolder.holder_location);
					jsonParams.put("user", tempHolder.holder_name);
					jsonParams.put("user_id", tempHolder.user_id);
					jsonParams.put("image", tempHolder.holder_image);
					jsonParams.put("store_id", tempHolder.store_id);
					StringEntity entity = new StringEntity(jsonParams.toString());
					
					DealFactory newFavedDeal = new DealFactory(
							timestamp, tempHolder.holder_image, tempHolder.holder_desc, tempHolder.holder_price, 
							tempHolder.holder_location,tempHolder.holder_name,tempHolder.store_id,tempHolder.user_id); 	
					FeedActivity.user_like_data.add(newFavedDeal);


					client.put(context,"http://198.61.177.186:8080/virgil/data/android/posts_liked_by_user/"+tempHolder.user_id+"/"+timestamp,entity,null,new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							Log.d("POST_LIKED_BY_USER:","Success HTTP PUT to POST_LIKED_BY_USER ColumnFamily");
						}
					});
				} catch (Exception e) {
					System.out.println("Failed HTTP PUT");
				} 
			}
		});

		return row;
	}

	static class FeedHolder{
		ImageView image;
		TextView desc;
		TextView price;
		TextView location;
		TextView user;
		ImageButton thumbs_up;
		
		String holder_timestamp;
		String holder_image;
		String holder_desc;
		String holder_price;
		String holder_location;
		String holder_name;
		String store_id;
		String user_id;
	}
	
	
	
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
