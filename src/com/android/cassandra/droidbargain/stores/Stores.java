package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;


public class Stores extends ListActivity {

	ArrayList<StoreFactory> store_data = new ArrayList<StoreFactory>();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		store_data.add(new StoreFactory("Forever 21"));
		store_data.add(new StoreFactory("La Maison Simons"));

		StoreAdapter adapter = new StoreAdapter(this,R.layout.store_item,store_data);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stores, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case android.R.id.home:
			Intent upIntent = new Intent(this, FeedActivity.class);
			startActivity(upIntent);
			break;
		}
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
	
//		Intent i = new Intent(getActivity(), VenueActivity.class);
//		
//		i.putExtra("THE_VENUE", venue_data[position]);
		
		startActivity(new Intent(this, StoreActivity.class));
	}

}
