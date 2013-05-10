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


public class StoreList extends ListActivity {

	private ArrayList<StoreFactory> store_data;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		store_data = FeedActivity.store_data;

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
	
		Intent i = new Intent(this, StoreActivity.class);
		
		i.putExtra("THE_STORE", store_data.get(position));
		
		startActivity(i);
		
	}

}
