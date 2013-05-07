package com.android.nitelights.wire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nitelights.R;



public class WireAdapter extends ArrayAdapter<WireFactory> {

	Context context;
	int layoutResourceId;
	WireFactory wireFactory[] = null;

	public WireAdapter(Context context, int layoutResourceId, WireFactory[] wireFactory){
		super(context, layoutResourceId, wireFactory);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.wireFactory = wireFactory;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		WireHolder holder = null;

		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new WireHolder();
			holder.element1 = (TextView) row.findViewById(R.id.element1);
			holder.element2 = (TextView) row.findViewById(R.id.element2);
			holder.wiretype = (TextView) row.findViewById(R.id.wiretype);
			
			row.setTag(holder);
		}
		else{
			holder = (WireHolder)row.getTag();
		}
		//For every item in the list. set Title, address and rating
		WireFactory wire = wireFactory[position];
		holder.element1.setText(wire.getElement1());
		holder.element2.setText(wire.getElement2());
		holder.wiretype.setText(wire.getWireType());

		return row;
	}

	static class WireHolder{
		TextView element1;
		TextView element2;
		TextView wiretype;
	}
}
