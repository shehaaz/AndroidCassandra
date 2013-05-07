package com.android.nitelights.venues;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nitelights.R;
/**
 * Extends the ArrayAdapter to take in VenueFactory objects (To obtain title and address)
 */
public class VenuesAdapter extends ArrayAdapter<VenuesFactory>{
	
	Context context;
	int layoutResourceId;
	VenuesFactory venueFactory[] = null;
	ImageView icon;
	boolean isDiscoball;
	
	
	
	public VenuesAdapter(Context context, int layoutResourceId, VenuesFactory[] venueFactory){
		super(context, layoutResourceId, venueFactory);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.venueFactory = venueFactory;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		VenueHolder holder = null;
		
		
		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new VenueHolder();
			holder.venueTitle = (TextView) row.findViewById(R.id.title_venue);
			holder.venueAddress = (TextView) row.findViewById(R.id.title_venue_address);
//			holder.venueIcon = (ImageView) row.findViewById(R.id.listitem_venue_icon);
		
			

			row.setTag(holder);
		}
		else{

			holder = (VenueHolder)row.getTag();
		}
		//For every venue is the list. set Title, address and rating
		VenuesFactory venue = venueFactory[position];
		holder.venueTitle.setText(venue.getTitle());
		holder.venueAddress.setText(venue.getAddress());
//		holder.venueIcon.setImageResource(venue.getLogo());
	
			
		return row;
	}
	
	static class VenueHolder{
		TextView venueTitle;
		TextView venueAddress;
		ImageView venueIcon;
	
	}
}
