package com.android.cassandra.droidbargain.feed;



import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.profile.Profile;
import com.android.cassandra.droidbargain.stores.StoreFactory;
import com.android.cassandra.droidbargain.stores.Stores;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FeedActivity extends ListActivity {

	private Context appContext;
	private ArrayList<FeedFactory> feed_data = new ArrayList<FeedFactory>();
	public static ArrayList<StoreFactory> store_data = new ArrayList<StoreFactory>();

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		appContext = getApplicationContext();

		AsyncHttpClient client = new AsyncHttpClient();

		client.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.495121,-73.580314&radius=500&types=book_store%7Cclothing_store%7Celectronics_store%7Cshoe_store%7Cjewelry_store&sensor=true&key=AIzaSyDMtyVzs_11fW_oye9hDLDu0OfPJXskBwg", 
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
						StoreFactory newStore = new StoreFactory(id,name,address,lat,lng);
						//System.out.println(newStore.toString());
						store_data.add(newStore);

						AsyncHttpClient client = new AsyncHttpClient();
						client.get("http://198.61.177.186:8080/virgil/data/android/posts/"+id,new AsyncHttpResponseHandler() {
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

											System.out.println(title + " " +desc+ " " + price + " " + location );

											feed_data.add(new FeedFactory(currentTimestamp, title, desc, price, location));	
											
										}
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

				}

				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


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
