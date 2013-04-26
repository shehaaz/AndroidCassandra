package com.android.cassandra.droidbargain;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;






public class InputActivity extends Activity {

	private User user;
	private Context context;
	private EditText usernameEditText;
	private EditText emailEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);



		usernameEditText = (EditText) findViewById(R.id.firstname);
		emailEditText = (EditText) findViewById(R.id.email);
		String lastName = (String) findViewById(R.id.lastname).toString();
		String age = (String) findViewById(R.id.age).toString();

		context = this.getApplicationContext();



		Button buttonDB = (Button) findViewById(R.id.putindb);
		buttonDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					
					user = new User(usernameEditText.getText().toString(), emailEditText.getText().toString());

					AsyncHttpClient client = new AsyncHttpClient();
					JSONObject jsonParams = new JSONObject();

					jsonParams.put(user.getAttName(), user.getName());
					jsonParams.put(user.getAttEmail(), user.getEmail());
					StringEntity entity = new StringEntity(jsonParams.toString());
					System.out.println(jsonParams.toString());

					client.put(context,"http://198.61.177.186:8080/virgil/data/android/users/5",entity,null,new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							System.out.println("Success HTTP PUT");
						}
					});

				} catch (Exception e) {
					System.out.println("Failed HTTP PUT");
				} 












				//		        AsyncHttpClient client = new AsyncHttpClient();
				//
				//		        client.get("http://198.61.177.186:8080/virgil/data/android/users/1", new AsyncHttpResponseHandler() {
				//		            @Override
				//		            public void onSuccess(String response) {
				//		                System.out.println(response);
				//		            }
				//		        });		        


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
