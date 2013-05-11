package com.android.cassandra.droidbargain.stores;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.android.cassandra.droidbargain.feed.FeedFactory;
import com.android.cassandra.droidbargain.input.InputActivity;
import com.android.cassandra.droidbargain.profile.Profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class StoreList extends ListActivity {

	private ArrayList<StoreFactory> store_data;

	private StoreFactory store;

	private ProgressDialog pDialog;

	private Intent intent;



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
		getMenuInflater().inflate(R.menu.feed_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case R.id.open_profile:
			startActivity(new Intent(this, Profile.class));
			return true;
		case R.id.open_stores:
			startActivity(new Intent(this, StoreList.class));
			return true;
		case R.id.open_camera:
			startActivity(new Intent(this, InputActivity.class));
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


		downloadData(store.getStoreID());


	}

	private void initializeDialog() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();	
	}

	private void downloadData(String store_id) {

		AsyncHttpClient cassandra_client = new AsyncHttpClient();
		cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts/"+store_id,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String  response) {

				if(response != null){

					JSONObject jObject;
					try {
						jObject = new JSONObject(response);

						Iterator<?> keys = jObject.keys();
						while(keys.hasNext()){
							String currentTimestamp = (String) keys.next();

							String postString = jObject.getString(currentTimestamp);
							JSONObject currentPostObject = new JSONObject(postString);

							String title = currentPostObject.getString("title");
							String desc = currentPostObject.getString("body"); 
							String price = currentPostObject.getString("price");
							String location = currentPostObject.getString("location");
							FeedFactory currFeedObj = new FeedFactory(currentTimestamp, title, desc, price, location); 	
							store.addDeal(currFeedObj);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				intent.putExtra("THE_STORE", store);
				startActivity(intent);
			}
		});

	}

}
