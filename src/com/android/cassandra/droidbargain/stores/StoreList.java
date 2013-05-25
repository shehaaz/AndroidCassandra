package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;
import com.android.cassandra.droidbargain.input.PhotoActivity;
import com.android.cassandra.droidbargain.profile.Profile;
import com.android.cassandra.droidbargain.profile.User;


public class StoreList extends ListActivity {

	private ArrayList<StoreFactory> store_data;

	private StoreFactory store;

	private ProgressDialog pDialog;

	private Intent intent;

	private User bargain_user;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		bargain_user = (User) getIntent().getSerializableExtra("USER_PROFILE");

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


		store_data = FeedActivity.store_data;

		StoreAdapter adapter = new StoreAdapter(this,R.layout.store_item,store_data);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case R.id.open_profile:
			Intent profileIntent = new Intent(this, Profile.class);
			profileIntent.putExtra("USER_PROFILE", bargain_user);
			startActivity(profileIntent);
			return true;
		case R.id.open_stores:
			Intent storeIntent = new Intent(this, StoreList.class);
			storeIntent.putExtra("USER_PROFILE", bargain_user);
			startActivity(storeIntent);
			return true;
		case R.id.open_camera:
			Intent postIntent = new Intent(this, PhotoActivity.class);
			postIntent.putExtra("USER_PROFILE", bargain_user);
			startActivity(postIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{

		intent = new Intent(this, StoreActivity.class);

		store = store_data.get(position);
		intent.putExtra("THE_STORE", store);

		startActivity(intent);


	}
}
