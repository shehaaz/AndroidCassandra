package com.android.cassandra.droidbargain.feed;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.cassandra.droidbargain.InputActivity;
import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.profile.Profile;
import com.android.cassandra.droidbargain.stores.StoreFactory;
import com.android.cassandra.droidbargain.stores.StoreList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FeedActivity extends ListActivity implements LocationListener {

	private Context appContext;
	public static ArrayList<FeedFactory> feed_data = new ArrayList<FeedFactory>();
	public static ArrayList<StoreFactory> store_data = new ArrayList<StoreFactory>();
	private String userLat;
	private String userLng;
	private LocationManager locationManager;
	private String provider;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		appContext = getApplicationContext();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		} else {
			userLat = "45.495121";
			userLng = "-73.580314";
		}

		if(!(this.getIntent().hasExtra("STORE_ID"))){
			initializeDialog();
			downloadData();
		}
		else {
			String store_id = getIntent().getStringExtra("STORE_ID");
			downloadNewData(store_id);
		}
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
			startActivity(new Intent(this, StoreList.class));
			return true;
		case R.id.open_camera:
			startActivity(new Intent(this, InputActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = (double) (location.getLatitude());
		double lng = (double) (location.getLongitude());
		userLat = String.valueOf(lat);
		userLng = String.valueOf(lng);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	private void initializeDialog() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();	
	}

	private void downloadNewData(String store_id) {

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
							feed_data.add(currFeedObj);
						}
						Collections.sort(feed_data);
						FeedAdapter adapter = new FeedAdapter(appContext,R.layout.feed_item,feed_data);
						setListAdapter(adapter);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

	}

	private void downloadData(){

		AsyncHttpClient googlePlaces_client = new AsyncHttpClient();

		googlePlaces_client.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+userLat+","+userLng+"&radius=500&types=book_store%7Cclothing_store%7Celectronics_store%7Cshoe_store%7Cjewelry_store&sensor=true&key=AIzaSyDMtyVzs_11fW_oye9hDLDu0OfPJXskBwg", 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {

				try {
					JSONArray resultsArray = response.getJSONArray("results");
					for(int i=0; i<resultsArray.length(); i++)
					{
						String name = resultsArray.getJSONObject(i).getString("name");
						String id = resultsArray.getJSONObject(i).getString("id");
						JSONObject geometry = resultsArray.getJSONObject(i).getJSONObject("geometry");
						JSONObject location = geometry.getJSONObject("location");
						Double lat = Double.parseDouble(location.getString("lat"));
						Double lng = Double.parseDouble(location.getString("lng"));
						String address = resultsArray.getJSONObject(i).getString("vicinity");
						final StoreFactory newStore = new StoreFactory(id,name,address,lat,lng);

						store_data.add(newStore);

						AsyncHttpClient cassandra_client = new AsyncHttpClient();
						cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts/"+id,new AsyncHttpResponseHandler() {
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
											feed_data.add(currFeedObj);
											newStore.addDeal(currFeedObj);
										}
										Collections.sort(feed_data);
										FeedAdapter adapter = new FeedAdapter(appContext,R.layout.feed_item,feed_data);
										setListAdapter(adapter);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							}
						});

					}
					pDialog.dismiss();
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}
