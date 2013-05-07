package com.android.nitelights.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.android.nitelights.profile.ProfileFactory;
import com.android.nitelights.ui.MainActivity;
import com.android.nitelights.venues.VenuesFactory;
import com.android.nitelights.wire.WireFactory;
import com.android.nitelights.wire.WireFriendFactory;
import com.android.nitelights.wire.WireVenueFactory;
/**
 * Background Async Task to Load all data from MySQL database by making HTTP Request
 * */
public class LoadMySQLData extends AsyncTask<Object, String, String> {

	VenuesFactory venue_data[];
	WireFactory wire_data[];
	WireFriendFactory wire_friends[];
	WireVenueFactory wire_venues[];
	ProfileFactory user_data;
	
	JSONParser jParser = new JSONParser();

	MainActivity callerActivity;

	//Data
	private String uid;
	
	private int venue_id;
	private String title;
	private String address;
	private double venue_lng;
	private double venue_lat; 
	private String number_commited; 
	private int rating;
	private String venue_photo;
	
	private String wire_name;
	private String wire_title;
	private String wire_timestamp;
	
	private String user_name;
	private String user_photo;
	private String user_photo_id;
	private String serviceUserCommit;
	private String num_venues;
	private String user_venue_commit;
	
	//service URLs
	private String serviceVenues;
	private String serviceWire;
	private String serviceUser;
	private String serviceWireFriendship;

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * getting All products from url
	 * */
	protected String doInBackground(Object... args) {

		callerActivity = (MainActivity) args[0];
		serviceVenues = (String) args[1];
		serviceWire = (String) args[2];
		uid = (String) args[3];
		serviceUser = (String) args[4];
		serviceWireFriendship = (String) args[5];
		serviceUserCommit = (String) args[6];

/**************************************************GET VENUES*****************************************************************************************/
		// Building Parameters
		List<NameValuePair> venue_params = new ArrayList<NameValuePair>();

		// getting JSON string from URL
		JSONObject jObject_Venues = jParser.makeHttpRequest(serviceVenues, "GET", venue_params);

		// Check your log cat for JSON reponse
//		Log.d("All Venues: ", jObject_Venues.toString());

		try {
			// Checking for SUCCESS TAG
			int success = jObject_Venues.getInt("success");

			if (success == 1) {
				// products found
				// Getting Array of venues
				JSONArray venues = jObject_Venues.getJSONArray("venues");
				venue_data = new VenuesFactory[venues.length()];

				// looping through All Products
				for (int i = 0; i < venues.length(); i++) {

					venue_id = Integer.parseInt(venues.getJSONObject(i).getString("nid"));
					title = venues.getJSONObject(i).getString("title");
					address = venues.getJSONObject(i).getString("field_address_thoroughfare");
					venue_lng = Double.parseDouble(venues.getJSONObject(i).getString("field_geo_lon"));
					venue_lat = Double.parseDouble(venues.getJSONObject(i).getString("field_geo_lat"));
					number_commited = venues.getJSONObject(i).getString("Number_Commited");
					rating = Integer.parseInt(venues.getJSONObject(i).getString("Rating"));
					String venue_photo_raw =  venues.getJSONObject(i).getString("logo");
					venue_photo = venue_photo_raw.substring(8);
					

					venue_data[i] = new VenuesFactory(venue_id,title,address,rating,venue_lat,venue_lng,number_commited,venue_photo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
/**************************************************GET WIRE Committed Venue*****************************************************************************************/
		// Building Parameters
		List<NameValuePair> wire_params = new ArrayList<NameValuePair>();
		wire_params.add(new BasicNameValuePair("uid", uid)); //entering as USER_Id = 1

		// getting JSON string from URL
		JSONObject jObject_Wire = jParser.makeHttpRequest(serviceWire, "GET", wire_params);

		// Check your log cat for JSON reponse
//		Log.d("All Wire_venue_data: ", jObject_Wire.toString());

		try {
			// Checking for SUCCESS TAG
			int success = jObject_Wire.getInt("success");

			if (success == 1) {
				// products found
				// Getting Array of venues
				JSONArray wire = jObject_Wire.getJSONArray("wire");
				wire_venues = new WireVenueFactory[wire.length()];

				// looping through All Products
				for (int i = 0; i < wire.length(); i++) {


					wire_name = wire.getJSONObject(i).getString("name");
					wire_title = wire.getJSONObject(i).getString("title");
					wire_timestamp = wire.getJSONObject(i).getString("timestamp");
					wire_venues[i] = new WireVenueFactory(wire_name,wire_title,"Committed To",wire_timestamp);
				}
			}
			else{
				wire_venues = new WireVenueFactory[] {new WireVenueFactory("","","No Commit Activity","0")};
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
/**************************************************GET WIRE Friendship data*****************************************************************************************/
		// Building Parameters
		List<NameValuePair> Friendship_params = new ArrayList<NameValuePair>();
		Friendship_params.add(new BasicNameValuePair("uid", uid)); //entering as USER_Id = 1

		// getting JSON string from URL
		JSONObject jObject_WireFriendship = jParser.makeHttpRequest(serviceWireFriendship, "GET", Friendship_params);

		// Check your log cat for JSON reponse
//		Log.d("All Wire_Friend_Data: ", jObject_WireFriendship.toString());

		try {
			// Checking for SUCCESS TAG
			int success = jObject_WireFriendship.getInt("success");

			if (success == 1) {
				// products found
				// Getting Array of venues
				JSONArray wire_friendship = jObject_WireFriendship.getJSONArray("wire");
				wire_friends = new WireFriendFactory[wire_friendship.length()];

				// looping through All Products
				for (int i = 0; i < wire_friendship.length(); i++) {


					String friend_name = wire_friendship.getJSONObject(i).getString("name");
					
					String wire_variables_raw = wire_friendship.getJSONObject(i).getString("variables");
					
					String firstDelims = "[><]";
					String[] Tokens = wire_variables_raw.split(firstDelims);
				
					wire_timestamp = wire_friendship.getJSONObject(i).getString("timestamp");
					wire_friends[i] = new WireFriendFactory(Tokens[2],Tokens[6],"is Friends With",wire_timestamp);
				}
			}
			else{
				wire_friends = new WireFriendFactory[] {new WireFriendFactory("","","No Friend Activity",null)};
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		

/**************************************************GET USER*****************************************************************************************/
		// Building Parameters
		List<NameValuePair> user_params = new ArrayList<NameValuePair>();
		user_params.add(new BasicNameValuePair("uid", uid)); //entering as USER_Id = 1

		// getting JSON string from URL
		JSONObject jObject_User = jParser.makeHttpRequest(serviceUser, "GET", user_params);

		// Check your log cat for JSON reponse
//		Log.d("All User_data: ", jObject_User.toString());

		try {
			// Checking for SUCCESS TAG
			int success = jObject_User.getInt("success");

			if (success == 1) {
				// products found
				// Getting Array of venues
				JSONArray user = jObject_User.getJSONArray("user");
				

				// looping through All Products
				for (int i = 0; i < user.length(); i++) {
					user_name = user.getJSONObject(i).getString("name");
					user_photo = user.getJSONObject(i).getString("picture_url");
					user_photo_id = user.getJSONObject(i).getString("picture_id");
					user_data = new ProfileFactory(user_name, user_photo, user_photo_id);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
/**************************************************GET Committed venue*****************************************************************************************/
		// Building Parameters
		List<NameValuePair> user_commit_params = new ArrayList<NameValuePair>();
		user_commit_params.add(new BasicNameValuePair("uid", uid)); //entering as USER_Id = 1

		// getting JSON string from URL
		JSONObject jObject_User_Commit = jParser.makeHttpRequest(serviceUserCommit, "GET", user_commit_params);


		try {
			// Checking for SUCCESS TAG
			int success = jObject_User_Commit.getInt("success");

			if (success == 1) {

				JSONArray user_commit = jObject_User_Commit.getJSONArray("user_commit");
				

				// looping through All Products
				for (int i = 0; i < user_commit.length(); i++) {
					num_venues = user_commit.getJSONObject(i).getString("total");
					user_venue_commit = user_commit.getJSONObject(i).getString("committed_venue");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "MySQLData_Success";
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String result) {
		
		//Setting the User's Commit
		String NewCommittedVenue = (!num_venues.equals("0")) ? user_venue_commit :  "Y U NO PARTY?";
		user_data.setCommittedVenue(NewCommittedVenue);
		
		//Copy venues and Friends data into wire
		int wire_venuesLen = wire_venues.length;
		int wire_friendsLen = wire_friends.length;
		wire_data = new WireFactory[wire_venuesLen + wire_friendsLen];
		System.arraycopy(wire_friends, 0, wire_data, 0, wire_friendsLen);
		System.arraycopy(wire_venues, 0, wire_data, wire_friendsLen, wire_venuesLen);
		
		
		//According to Decending Timestamp value
		Arrays.sort(wire_data);
		
		Arrays.sort(venue_data);

				
		callerActivity.setVenues(venue_data);
		callerActivity.setWire(wire_data);
		callerActivity.setUser(user_data);
		callerActivity.setAdapter();
	}
}
