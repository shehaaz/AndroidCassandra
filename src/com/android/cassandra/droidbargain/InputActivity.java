package com.android.cassandra.droidbargain;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;






public class InputActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		
        String firstName = (String) findViewById(R.id.firstname).toString();
        String lastName = (String) findViewById(R.id.lastname).toString();
        String age = (String) findViewById(R.id.age).toString();
        String email = (String) findViewById(R.id.email).toString();
        
        try {
        	
        	AsyncHttpClient client = new AsyncHttpClient();
        	JSONObject jsonParams = new JSONObject();
        	jsonParams.put("name", "rifaadh");
        	jsonParams.put("email", "rifaadh@gmail.com");
        	StringEntity entity = new StringEntity(jsonParams.toString());
        	System.out.println(jsonParams.toString());
			client.put(this.getApplicationContext(),"http://198.61.177.186:8080/virgil/data/android/users/2",entity,"application/json",new AsyncHttpResponseHandler() {
	            @Override
	            public void onSuccess(String response) {
	                System.out.println(response);
	            }
	        });
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Button buttonDB = (Button) findViewById(R.id.putindb);
        buttonDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				

				
				
				
//		        AsyncHttpClient client = new AsyncHttpClient();
//
//		        client.get("http://198.61.177.186:8080/virgil/data/android/users/1", new AsyncHttpResponseHandler() {
//		            @Override
//		            public void onSuccess(String response) {
//		                System.out.println(response);
//		            }
//		        });
//		        
//		        RequestParams params = new RequestParams();
//		        params.put("name", "rifaadh");
//		       "{\"name\":\"rif\",\"email\":\"rif@gmail.com\"}"
		             		        
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
