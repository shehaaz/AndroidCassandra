package com.android.nitelights.venues;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.nitelights.R;
import com.android.nitelights.ui.MainActivity;



/**
 * Venue fragment
 */
public class VenuesFragment extends ListFragment{

	VenuesFactory venue_data[];
	private VenuesAdapter adapter;
	public static boolean changeCommit = true;
	public static String committedVenue;
	

	//Constructor that gets the static venue_data fetched by the MainActivity
	public VenuesFragment(){
		this.venue_data = MainActivity.getVenues();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.list_venues,container, false);
		return rootView;
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		adapter =  new VenuesAdapter(getActivity(), R.layout.list_item_venues, this.venue_data);
		setListAdapter(adapter);
		setRetainInstance(true);
	}


	//Here when the venue item is clicked open the map at the location of the venue
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
	
		Intent i = new Intent(getActivity(), VenueActivity.class);
		
		i.putExtra("THE_VENUE", venue_data[position]);
		
		startActivity(i);
	}
}
