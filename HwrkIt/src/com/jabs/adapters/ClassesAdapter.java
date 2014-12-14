package com.jabs.adapters;

import java.util.ArrayList;
import java.util.Random;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jabs.hwrkit.ClassFragment;
//import com.jabs.hwrkit.MainActivity;
//import com.jabs.hwrkit.MainActivity.PlaceholderFragment;
import com.jabs.hwrkit.R;
import com.jabs.structures.Class;
import com.jabs.structures.LetterAvatar;

public class ClassesAdapter extends ArrayAdapter<Class>{
	private final static int CLASSES_ROW_LAYOUT = R.layout.row_layout;
	private List<Class> classList;
	private int color = Color.parseColor("#0066cc");
	
	public ClassesAdapter(Context context, List<Class> classList){
		super(context, CLASSES_ROW_LAYOUT, classList);
		this.classList = classList;
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
		final TextView className = (TextView) rowView.findViewById(R.id.className);
		final ImageView artwork = (ImageView) rowView.findViewById(R.id.classArt);
		
		// set the text of the views
		className.setText(curr.getClassName());
		int color;
		
		// if color has been set
		if(curr.getColor() != 0){
			color = curr.getColor();
		}else{
			// default is blue
			color = Color.parseColor("#FDD835");  
		}
		artwork.setImageDrawable(new LetterAvatar(artwork.getContext(), color, curr.getClassName().substring(0,1), 7));
		
		// do the artwork
		return rowView;
	}
	
}
