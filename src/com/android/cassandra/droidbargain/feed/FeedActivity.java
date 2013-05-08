package com.android.cassandra.droidbargain.feed;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.profile.Profile;
import com.android.cassandra.droidbargain.stores.Stores;

public class FeedActivity extends ListActivity {

	FeedFactory feed_data[] = {new FeedFactory("hello", "world"),new FeedFactory("goodbye", "world")};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		FeedAdapter adapter = new FeedAdapter(this,R.layout.feed_item,feed_data);
		setListAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){

		case R.id.open_profile:
			startActivity(new Intent(this, Profile.class));
			return true;
		case R.id.open_stores:
			startActivity(new Intent(this, Stores.class));
			return true;
		case R.id.open_camera:
			startActivity(new Intent(this, Profile.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
