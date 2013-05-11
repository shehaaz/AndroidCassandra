package com.android.cassandra.droidbargain.input;

import java.util.ArrayList;

import com.android.cassandra.droidbargain.stores.StoreFactory;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinAdapter extends ArrayAdapter<StoreFactory>{

   
    private Context context;
    private ArrayList<StoreFactory> values;

    public SpinAdapter(Context context, int textViewResourceId,
            ArrayList<StoreFactory> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.size();
    }

    public StoreFactory getItem(int position){
       return values.get(position);
    }

    public long getItemId(int position){
       return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	       
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(25);

        label.setText(values.get(position).getStoreTitle());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setText(values.get(position).getStoreTitle());

        return label;
    }
}
