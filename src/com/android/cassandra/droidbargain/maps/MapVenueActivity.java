package com.android.nitelights.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nitelights.R;
import com.android.nitelights.ui.MainActivity;
import com.android.nitelights.venues.VenuesFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapVenueActivity extends Activity {


	 
	  private GoogleMap map;
	  private LatLng venueLocation;
	  double lat;
	  double lng;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_map);
	    
		
		final ActionBar actionBar = getActionBar();
		
		//show the home button
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	        .getMap();
		
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    map.getUiSettings().setZoomControlsEnabled(true);
	    map.getUiSettings().setCompassEnabled(true);
	    
	    Bundle bundle = getIntent().getExtras();
		VenuesFactory venue = bundle.getParcelable("VENUE_LOCATION");
		lat = venue.getLat();
		lng = venue.getLng();
		venueLocation = new LatLng(lat,lng);
		
		
	    map.addMarker(new MarkerOptions().position(venueLocation)
	            .title(venue.getTitle())
	            .snippet(venue.getAddress())
	            .icon(BitmapDescriptorFactory
	            		.fromResource(R.drawable.new_map_marker)));

	    // Move the camera instantly to mcgill with a zoom of 15.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	    
	   
	  }

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.activity_map, menu);
	    return true;
	  }
	  
	  //This hook is called whenever an item in your opetions menu is selected
	  public boolean onOptionsItemSelected(MenuItem item) {

		  switch(item.getItemId()){
		  case android.R.id.home:
			  Intent upIntent = new Intent(this, MainActivity.class);
			  startActivity(upIntent);
			  break;
		  }
		  return true;
	  }
}
