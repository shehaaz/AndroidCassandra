package com.android.nitelights.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.nitelights.R;
import com.android.nitelights.database.LoadMySQLData;
import com.android.nitelights.login.LoginActivity;
import com.android.nitelights.maps.MapActivity;
import com.android.nitelights.profile.ProfileFactory;
import com.android.nitelights.profile.ProfileFragment;
import com.android.nitelights.venues.VenuesFactory;
import com.android.nitelights.venues.VenuesFragment;
import com.android.nitelights.wire.WireFragment;
import com.android.nitelights.wire.WireFactory;

/**
 * 
 * @author Shehaaz
 *The starting point of the application. It branches to the other view Fragments in the package
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	static AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	public static VenuesFactory venue_data[];
	public static WireFactory wire_data[];
	public static ProfileFactory user_data;
	private String url_all_venues = "http://niteflow.com/AndroidDB/get_all_venues.php";
	private String url_user_wire_data = "http://niteflow.com/AndroidDB/get_user_wire_data.php";
	private String url_user_data = "http://niteflow.com/AndroidDB/get_user_info_from_uid.php";
	private String url_user_wire_friendships = "http://niteflow.com/AndroidDB/get_user_wire_friendships.php";
	private String url_user_committed_venue = "http://niteflow.com/AndroidDB/get_user_commit.php";
	static ViewPager mViewPager;
	public static String committedVenue;
	private ProgressDialog pDialog;
	private String uid;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set userID from loginActivity
		uid = LoginActivity.uid;

		//Initialize Dialog and Start Web Service
		initializeDialog();
		startWebService();
		

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			public void onPageSelected(int position){
				actionBar.setSelectedNavigationItem(position);
			}
		});

		
		//naming the tabs of the ActionBar
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_the_wire)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_venues)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_profile)
				.setTabListener(this));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		
		case R.id.menu_map:
			Intent i = new Intent(this, MapActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void initializeDialog() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();	
	}

	private void startWebService() {
		//Load All venues in background thread
		new LoadMySQLData().execute(this,url_all_venues,url_user_wire_data,uid,url_user_data,url_user_wire_friendships,url_user_committed_venue);
	}

	public void setVenues(VenuesFactory[] pVenues){
		venue_data = pVenues;
	}
	
	public static VenuesFactory[] getVenues(){
		return venue_data;
	}
	
	public void setWire(WireFactory[] pWire){
		wire_data = pWire;
	}
	
	public static WireFactory[] getWire(){
		return wire_data;
	}
	
	public void setUser(ProfileFactory pUser_data) {
		user_data = pUser_data;
	}
	
	public static ProfileFactory getUser(){
		return user_data;
	}
	
	public void setAdapter(){
		//After Loading the Venues. Set pager Adapter that will supply views for this pager as needed
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		pDialog.dismiss();
	}

	/**
	 * 
	 * @author Lenovo
	 * Implementation of PagerAdapter(Base Class providing the adapter to populate pages inside
	 * of a ViewPager) that represents each page as a Fragment that is persistently kept in 
	 * the fragment manager as long as the user can return to the page.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		private static final int NUM_SECTIONS = 3;

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			switch(i) {

			case 0:
				Fragment wireFragment = new WireFragment();
				return wireFragment;

			case 1:
				Fragment venueFragment = new VenuesFragment();
				return venueFragment;
			case 2: 	
				Fragment profileFragment = new ProfileFragment();
				return profileFragment;

			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return NUM_SECTIONS;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle state){
		super.onSaveInstanceState(state);
		committedVenue = VenuesFragment.committedVenue;
	}
	
	//*********Below are the Action Bar methods*****************//
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}


}
