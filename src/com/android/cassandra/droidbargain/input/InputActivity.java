package com.android.cassandra.droidbargain.input;

import java.util.Calendar;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.R.id;
import com.android.cassandra.droidbargain.R.layout;
import com.android.cassandra.droidbargain.R.menu;
import com.android.cassandra.droidbargain.feed.FeedActivity;
import com.android.cassandra.droidbargain.stores.StoreFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;






public class InputActivity extends Activity {

	private Context context;
	private EditText title;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		alertDialogBuilder = new AlertDialog.Builder(this);

		calendar = Calendar.getInstance();

		title = (EditText) findViewById(R.id.post_title);
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

				StoreFactory store = adapter.getItem(position);
				storeID = store.getStoreID();
				location = store.getStoreTitle();

			}
			@Override
			public void onNothingSelected(AdapterView<?> adapter) {  }
		});


		Button buttonDB = (Button) findViewById(R.id.putindb);
		buttonDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String postTitle = title.getText().toString();
				String postBody = body.getText().toString();
				String postPrice = price.getText().toString();

				if(!postTitle.isEmpty() && !postBody.isEmpty() && !postPrice.isEmpty()){

					try {
						AsyncHttpClient client = new AsyncHttpClient();
						JSONObject jsonParams = new JSONObject();
						timestamp = String.valueOf(calendar.getTimeInMillis());
						jsonParams.put("title", postTitle);
						jsonParams.put("body", postBody);
						jsonParams.put("price", postPrice);
						jsonParams.put("location", location);
						StringEntity entity = new StringEntity(jsonParams.toString());
						System.out.println(jsonParams.toString());

						client.put(context,"http://198.61.177.186:8080/virgil/data/android/posts/"+storeID+"/"+timestamp,entity,null,new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println("Success HTTP PUT");
								Intent i = new Intent(context, FeedActivity.class);
								i.putExtra("STORE_ID",storeID);
								startActivity(i);
								finish();
							}
						});

					} catch (Exception e) {
						System.out.println("Failed HTTP PUT");
					} 
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
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

}
