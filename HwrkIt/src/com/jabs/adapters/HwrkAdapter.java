package com.jabs.adapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jabs.hwrkit.HwrkFragment;
import com.jabs.hwrkit.R;
import com.jabs.structures.Course;
import com.jabs.structures.HwrkTime;

public class HwrkAdapter extends ArrayAdapter<HwrkTime>{
	private final static int CLASSES_ROW_LAYOUT = R.layout.row_layout;
	private LinkedList<HwrkTime> times;
	
	public HwrkAdapter(Context context, LinkedList<HwrkTime> hwrkList){
		super(context, CLASSES_ROW_LAYOUT, hwrkList);
		this.times = hwrkList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) { // first time: inflate a new View
			Context context = getContext();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE
					);
			rowView = inflater.inflate(CLASSES_ROW_LAYOUT, null);
		}
		// populate the class list view
		
		// get the current class
		HwrkTime curr = getItem(position);
		
		// get references to views on page
		final TextView className = (TextView) rowView.findViewById(R.id.className);
		
		// set the text of the views
		className.setText(curr.getCourse().getClassName());
		// do the artwork
		return rowView;
	}
	
}
