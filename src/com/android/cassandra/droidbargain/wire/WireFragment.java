package com.android.nitelights.wire;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitelights.R;
import com.android.nitelights.ui.MainActivity;

public class WireFragment extends ListFragment{
	
WireFactory wire_data[];

	public WireFragment(){
		this.wire_data = MainActivity.getWire();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.list_wire,container, false);
					
		return rootView;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		
		WireAdapter adapter = new WireAdapter(getActivity(), R.layout.list_item_wire, wire_data);
		    
		setListAdapter(adapter);
			
	}
	

}
