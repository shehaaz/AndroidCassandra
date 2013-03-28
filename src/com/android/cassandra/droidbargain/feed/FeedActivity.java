package com.android.cassandra.droidbargain.feed;

import com.android.cassandra.droidbargain.R;
import com.android.cassandra.droidbargain.R.layout;
import com.android.cassandra.droidbargain.R.menu;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class FeedActivity extends ListActivity {
	
	FeedFactory feed_data[] = {
								new FeedFactory("hello", "world"),
								new FeedFactory("goodbye", "world")
							  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        
        FeedAdapter adapter = new FeedAdapter(this,R.layout.feed_item,feed_data);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }
    
}
