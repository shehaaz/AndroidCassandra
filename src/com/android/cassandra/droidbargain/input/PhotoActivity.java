package com.android.cassandra.droidbargain.input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.feed.FeedActivity;
import com.android.cassandra.droidbargain.profile.User;
import com.android.cassandra.droidbargain.stores.StoreFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class PhotoActivity extends Activity {

	private Context context;
	private EditText body;
	private EditText price;
	private SpinAdapter adapter;
	private Spinner mySpinner;
	private String storeID;
	private String timestamp;
	private String location;
	private Calendar calendar;
	private Context appContext;
	private AlertDialog.Builder alertDialogBuilder;
	private User bargain_user;
	private String user_ID;
	private StoreFactory store;
	private String postBody;
	private String postPrice;

	private static final int ACTION_TAKE_PHOTO_B = 1;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";


	private static final String VIDEO_STORAGE_KEY = "viewvideo";
	private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";


	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}


	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	private String setPic() {


		BitmapFactory.Options bmOptions = new BitmapFactory.Options();

		int scaleFactor = 4;

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

		byte [] ba = bao.toByteArray();

		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);

		return ba1;
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			File f = null;

			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch

		startActivityForResult(takePictureIntent, actionCode);
	}



	private String handleBigCameraPhoto() {

		String base64Image = "";

		if (mCurrentPhotoPath != null) {
			base64Image = setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}

		return base64Image;
	}


	Button.OnClickListener mTakePicOnClickListener = 
			new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			postBody = body.getText().toString();
			postPrice = price.getText().toString();

			if(!postBody.isEmpty() && !postPrice.isEmpty()){
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
			}
			else{
				alertDialogBuilder.setTitle("Please Fill all Fields");
				alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	};


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		bargain_user = (User) getIntent().getSerializableExtra("USER_PROFILE");
		user_ID = bargain_user.getUser_ID();

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		alertDialogBuilder = new AlertDialog.Builder(this);

		calendar = Calendar.getInstance();

		body = (EditText) findViewById(R.id.post_body);
		price = (EditText) findViewById(R.id.post_price);



		context = this.getApplicationContext();
		//custom Adapter opject: SpinAdapter
		adapter = new SpinAdapter(context,android.R.layout.simple_spinner_item,FeedActivity.store_data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner = (Spinner) findViewById(R.id.miSpinner);
		mySpinner.setAdapter(adapter);



		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {

				store = adapter.getItem(position);
				storeID = store.getStoreID();
				location = store.getStoreTitle();

			}
			@Override
			public void onNothingSelected(AdapterView<?> adapter) {  }
		});


		Button picBtn = (Button) findViewById(R.id.send_post);
		setBtnListenerOrDisable( 
				picBtn, 
				mTakePicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
				);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {
				String base64Image = handleBigCameraPhoto();
				//Start input Activity here
				Log.d("BASE64IMAGE", base64Image);
				try {
					AsyncHttpClient client = new AsyncHttpClient();
					JSONObject jsonParams = new JSONObject();
					timestamp = String.valueOf(calendar.getTimeInMillis());
					jsonParams.put("body", postBody);
					jsonParams.put("price", postPrice);
					jsonParams.put("location", location);
					jsonParams.put("user", bargain_user.getName());
					jsonParams.put("image", base64Image);
					StringEntity entity = new StringEntity(jsonParams.toString());


					client.put(context,"http://198.61.177.186:8080/virgil/data/android/posts/"+storeID+"/"+timestamp,entity,null,new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							Log.d("POST:","Success HTTP PUT to POST ColumnFamily");

						}
					});

					client.put(context,"http://198.61.177.186:8080/virgil/data/android/posts_by_user/"+user_ID+"/"+timestamp,entity,null,new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							Log.d("POST:","Success HTTP PUT to POSTS_BY_USER ColumnFamily");
							Intent i = new Intent(context, FeedActivity.class);
							i.putExtra("THE_STORE",store);
							i.putExtra("TIMESTAMP", timestamp);
							startActivity(i);
							finish();
						}
					});

				} catch (Exception e) {
					System.out.println("Failed HTTP PUT");
				} 
			}
			break;
		} 	
		} 
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void setBtnListenerOrDisable( 
			Button btn, 
			Button.OnClickListener onClickListener,
			String intentName
			) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setText( 
					getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}

}