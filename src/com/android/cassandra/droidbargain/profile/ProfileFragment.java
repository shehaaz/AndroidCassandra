package com.android.cassandra.droidbargain.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;


public class ProfileFragment extends Fragment {


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.profile,container, false);


		User user = (User) getActivity().getIntent().getSerializableExtra("USER_PROFILE");
		TextView name = (TextView) rootView.findViewById(R.id.profileName);
		name.setText(user.getName());
		
		ImageView user_image = (ImageView) rootView.findViewById(R.id.profilePicture);
		user_image.setImageBitmap(FeedActivity.user_image);
		

		return rootView;
	}
}
