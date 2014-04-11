package com.raweng.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidstackscrollview.R;


public class MyCustomAdapter extends ArrayAdapter<String> {
	
	int color ;

	public MyCustomAdapter(Context dataViewFragment, int invalid,	String[] objects,int white) {
		super(dataViewFragment, invalid, objects);
		// TODO Auto-generated constructor stub
		this.color = white;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		View row = convertView;
		
		if(row==null){
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.row, parent, false);
		}
		TextView tv = (TextView) row.findViewById(R.id.textView1);
		tv.setText(Shakespeare.TITLES[position]);
		tv.setTextColor(color);
		
		return row;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}
}
