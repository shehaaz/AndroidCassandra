package com.android.cassandra.droidbargain;

import java.util.Calendar;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;






public class InputActivity extends Activity {

	private Context context;
	private EditText title;
	private EditText desc;
	private SpinAdapter adapter;
	private Spinner mySpinner;
	private String storeID;
	private String timestamp;
	private String location;
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		
		calendar = Calendar.getInstance();
		
		title = (EditText) findViewById(R.id.firstname);
		desc = (EditText) findViewById(R.id.email);


		context = this.getApplicationContext();
		//custom Adapter opject: SpinAdapter
		adapter = new SpinAdapter(context,android.R.layout.simple_spinner_dropdown_item,FeedActivity().store_data);
		//Add Spinner to activity_input layout
		//<Spinner android:id="@+id/miSpinner" android:layout_width="wrap_content" android:layout_height="wrap_content"></Spinner>
		mySpinner = (Spinner) findViewById(R.id.miSpinner);
		mySpinner.setAdapter(adapter);
		
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view,
	                    int position, long id) {
	                // Here you get the current item (a User object) that is selected by its position
	                StoreFactory store = adapter.getItem(position);
	                // Here you can do the action you want to...
	                //Toast.makeText(Main.this, "ID: " + user.getId() + "\nName: " + user.getName(),
	                    //Toast.LENGTH_SHORT).show();
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

				try {
					AsyncHttpClient client = new AsyncHttpClient();
					JSONObject jsonParams = new JSONObject();
					timestamp = String.valueOf(calendar.getTimeInMillis());
					jsonParams.put("title", title.getText().toString());
					jsonParams.put("desc", desc.getText().toString());
					jsonParams.put("location", location);
					StringEntity entity = new StringEntity(jsonParams.toString());
					System.out.println(jsonParams.toString());

                           client.put(context,"http://198.61.177.186:8080/virgil/data/android/posts/"+storeID+"/"+timestamp,entity,null,new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							System.out.println("Success HTTP PUT");
						}
					});

				} catch (Exception e) {
					System.out.println("Failed HTTP PUT");
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

}
