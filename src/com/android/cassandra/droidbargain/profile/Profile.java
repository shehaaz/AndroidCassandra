package com.android.cassandra.droidbargain.profile;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedFactory;
import com.android.cassandra.droidbargain.input.InputActivity;
import com.android.cassandra.droidbargain.stores.StoreList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class Profile extends FragmentActivity implements ActionBar.TabListener {

	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private static ViewPager mViewPager;
	private User bargain_user;
	protected static ArrayList<FeedFactory> user_deal_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		bargain_user = (User) getIntent().getSerializableExtra("USER_PROFILE");

		downloadData(bargain_user.getUser_ID());


		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
				.setText(R.string.title_profile)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.my_deals)
				.setTabListener(this));		
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
			Intent postIntent = new Intent(this, InputActivity.class);
			postIntent.putExtra("USER_PROFILE", bargain_user);
			startActivity(postIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		private static final int NUM_SECTIONS = 2;

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			switch(i) {

			case 0:
				return new ProfileFragment();

			case 1:
				return new MyDealsFragment();

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
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	private void downloadData(String user_id) {
		
		 user_deal_data = new ArrayList<FeedFactory>();

		AsyncHttpClient cassandra_client = new AsyncHttpClient();
		cassandra_client.get("http://198.61.177.186:8080/virgil/data/android/posts_by_user/"+user_id,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String  response) {

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
						user_deal_data.add(currFeedObj);
					}
					mViewPager.setAdapter(mAppSectionsPagerAdapter);
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}
