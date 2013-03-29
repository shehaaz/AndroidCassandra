package com.android.cassandra.droidbargain;

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
        
        
        Button buttonDB = (Button) findViewById(R.id.putindb);
        buttonDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//send text to db
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
