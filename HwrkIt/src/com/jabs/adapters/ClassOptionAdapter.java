package com.jabs.adapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jabs.hwrkit.HwrkFragment;
import com.jabs.hwrkit.R;
import com.jabs.structures.Class;
import com.jabs.structures.HwrkTime;

public class ClassOptionAdapter extends ArrayAdapter<Class>{
	private final static int CLASSES_ROW_LAYOUT = android.R.layout.simple_list_item_1;
	private ArrayList<Class> classes;
	private HwrkFragment parent;
	
	public ClassOptionAdapter(Context context, ArrayList<Class> classes, HwrkFragment parent, Dialog ref){
		super(context, CLASSES_ROW_LAYOUT, classes);
		this.classes = classes;
		this.parent = parent;
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
		Class curr = getItem(position);
		
		// get references to views on page
		final TextView className = (TextView) rowView.findViewById(android.R.id.text1);
		
		// set the text of the views
		className.setText(curr.getClassName());
		// do the artwork
		return rowView;
	}
	
	public void closeModal(){
		parent.classDialog.dismiss();
	}
	
}
