package com.android.cassandra.droidbargain.feed;



import java.io.InputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.input.PhotoActivity;
import com.android.cassandra.droidbargain.profile.Profile;
import com.android.cassandra.droidbargain.profile.User;
import com.android.cassandra.droidbargain.stores.StoreFactory;
import com.android.cassandra.droidbargain.stores.StoreList;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FeedActivity extends ListActivity implements LocationListener {

	private Context appContext;
	private ArrayList<DealFactory> feed_data = new ArrayList<DealFactory>();
	public static ArrayList<StoreFactory> store_data = new ArrayList<StoreFactory>();
	public static ArrayList<DealFactory> user_deal_data;
	public static Bitmap user_image;
	private String userLatLng;
	private ProgressDialog pDialog;
	private LocationManager mLocationManager;
	private String user_Name;
	private String user_ID;
	private User bargain_user;
	public static boolean downloadPhoto = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		//Start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			//callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					//make request to the /me PAI
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user!=null){

								user_Name = user.getName();
								user_ID = user.getId();
								downloadUserData(user_ID);
								bargain_user = new User(user_Name, user_ID);
								new DownloadImageTask().execute(bargain_user.getUserPhoto());
							}
						}
					});
				}
			}
		});

		appContext = getApplicationContext();


		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// new GetUserLocation(this, mLocationManager).getUserLocation();
		//"45.49515,-73.577558";
		userLatLng = new GetUserLocation(this, mLocationManager).getUserLocation();



		if(!(this.getIntent().hasExtra("NEW_POST"))){
			initializeDialog();
			downloadStores();
		}
		else {
			initializeDialog();
			downloadStoreData();
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
	
	private void downloadUserData(String user_id) {

		user_deal_data = new ArrayList<DealFactory>();

		AsyncHttpClient cassandra_client = new AsyncHttpClient();
		cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts_by_user/"+user_id,new AsyncHttpResponseHandler() {
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

							String image = currentPostObject.getString("image");
							String desc = currentPostObject.getString("body"); 
							String price = currentPostObject.getString("price");
							String location = currentPostObject.getString("location");
							String user = currentPostObject.getString("user");
							String storeID = currentPostObject.getString("store_id");
							String userID = currentPostObject.getString("user_id");
							DealFactory currFeedObj = new DealFactory(currentTimestamp, image, desc, price, location,user,storeID,userID); 	
							user_deal_data.add(currFeedObj);
						}
						
					}
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});

	}

	private void downloadStoreData() {


		AsyncHttpClient cassandra_client = new AsyncHttpClient();

		for(StoreFactory store : store_data){
			cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts/"+store.getStoreID(),new AsyncHttpResponseHandler() {
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

								String image = currentPostObject.getString("image");
								String desc = currentPostObject.getString("body"); 
								String price = currentPostObject.getString("price");
								String location = currentPostObject.getString("location");
								String user = currentPostObject.getString("user");
								String storeID = currentPostObject.getString("store_id");
								String userID = currentPostObject.getString("user_id");
								DealFactory currDealObj = new DealFactory(currentTimestamp, image, desc, price, location,user,storeID,userID); 
								feed_data.add(currDealObj);
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

	private void downloadStores(){

		AsyncHttpClient googlePlaces_client = new AsyncHttpClient();

		googlePlaces_client.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+userLatLng+"&radius=500&types=book_store%7Cclothing_store%7Celectronics_store%7Cshoe_store%7Cjewelry_store&sensor=true&key=AIzaSyDMtyVzs_11fW_oye9hDLDu0OfPJXskBwg", 
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
						AsyncHttpClient cassandra_client = new AsyncHttpClient();

						cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts/"+newStore.getStoreID(),new AsyncHttpResponseHandler() {
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

											String image = currentPostObject.getString("image");
											String desc = currentPostObject.getString("body"); 
											String price = currentPostObject.getString("price");
											String location = currentPostObject.getString("location");
											String user = currentPostObject.getString("user");
											String storeID = currentPostObject.getString("store_id");
											String userID = currentPostObject.getString("user_id");
											DealFactory currDealObj = new DealFactory(currentTimestamp, image, desc, price, location,user,storeID,userID); 
											newStore.addDeal(currDealObj);
											feed_data.add(currDealObj);
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
						store_data.add(newStore);
					}
					
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pDialog.dismiss();
			}
		});
	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			user_image = result;
		}
	}

}
