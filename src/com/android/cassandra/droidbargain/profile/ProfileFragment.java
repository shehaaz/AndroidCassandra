package com.android.nitelights.profile;

import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nitelights.R;
import com.android.nitelights.login.DrupalConnect;
import com.android.nitelights.login.LoginActivity;
import com.android.nitelights.ui.MainActivity;

/**
 * Profile fragment
 */
public class ProfileFragment extends Fragment{

	ProfileFactory user;
	private ProgressDialog pDialog;

	public ProfileFragment(){
		user = MainActivity.getUser();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.profile,container, false);

		
		
		TextView name = (TextView) rootView.findViewById(R.id.profileName);
		name.setText(user.getName());

		if(!user.getPhotoID().equals("0")){
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.profilePicture))
		.execute(user.getProfilePhoto());
		}
		
		TextView committed_venue = (TextView) rootView.findViewById(R.id.profileCommit);
		committed_venue.setText(user.getCommittedVenue());
		

		Button logoutButton = (Button) rootView.findViewById(R.id.buttonSignOut);

		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initializeDialog();
				// create an async task to perform login
				(new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						try {
							DrupalConnect.getInstance().logout();
							return "true";
						}
						catch (Exception ex) {

						}
						return null;
					}

					@Override
					protected void onPostExecute(String result) {
						pDialog.dismiss();
						Intent i = new Intent(getActivity(),LoginActivity.class);
						startActivity(i);
					}
				}).execute();
			}

		});
		return rootView;
	}

	private void initializeDialog() {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Logging Out...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();	
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

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
			bmImage.setImageBitmap(result);
		}
	}

}
