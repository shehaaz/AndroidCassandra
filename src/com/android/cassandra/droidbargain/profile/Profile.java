package com.android.cassandra.droidbargain.profile;

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

import com.android.cassandra.droidbargain.InputActivity;
import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;
import com.android.cassandra.droidbargain.stores.StoreList;

public class Profile extends FragmentActivity implements ActionBar.TabListener {

	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private static ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

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

		mViewPager.setAdapter(mAppSectionsPagerAdapter);

		//naming the tabs of the ActionBar
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_profile)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_liked_deals)
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
				return new FavDealsFragment();

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

}
