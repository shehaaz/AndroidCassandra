package com.android.cassandra.droidbargain.feed;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.input.InputActivity;
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
	public static ArrayList<FeedFactory> feed_data = new ArrayList<FeedFactory>();
	public static ArrayList<StoreFactory> store_data = new ArrayList<StoreFactory>();
	private String userLatLng;
	private LocationManager locationManager;
	private String provider;
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

								user_Name = user.getMiddleName()+" "+user.getLastName();
								user_ID = user.getId();

								bargain_user = new User(user_Name, user_ID);
							}
						}
					});
				}
			}
		});

		appContext = getApplicationContext();


		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// new GetUserLocation(this, mLocationManager).getUserLocation();
		userLatLng = "45.49515,-73.577558";


		if(!(this.getIntent().hasExtra("STORE_ID"))){
			initializeDialog();
			downloadStores();
		}
		else {
			String store_id = getIntent().getStringExtra("STORE_ID");
			downloadStoreData(store_id);
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
			Intent postIntent = new Intent(this, InputActivity.class);
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

	private void downloadStoreData(String store_id) {

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

						store_data.add(newStore);
						downloadStoreData(id);
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


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
