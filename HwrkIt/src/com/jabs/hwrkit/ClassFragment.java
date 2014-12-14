package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.globals.User;
import com.jabs.structures.Class;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ClassFragment extends Fragment {
	public ClassFragment() {

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View ret = inflater.inflate(R.layout.fragment_classes, container,
				false);
		
		// get instance of singleton
		User theUser = User.getInstance();
		
		// get the classes belonging to the user
		final List<Class> classList = theUser.getClasses();
		final int[] colorArray = {Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA,
								  Color.RED, Color.YELLOW}; 
		// get resources
		ListView list = (ListView) ret.findViewById(R.id.classList);
		Button addClasses = (Button) ret.findViewById(R.id.addClass);
		
		// set the adapter for classes listview
		ClassesAdapter adapter = new ClassesAdapter(this.getActivity(), classList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
						
				Bundle passClass = new Bundle();
				passClass.putInt("classPosition", position);
				
				StatisticsFragment classStats = new StatisticsFragment();
				classStats.setArguments(passClass);
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						classStats).commit();
				
			}
			
		});
		
		addClasses.setOnTouchListener(new OnTouchListener () {
		  	@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					// set drawable
			    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
			 	
			        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			        // Get the layout inflater
			        LayoutInflater inflater = getActivity().getLayoutInflater();
			        
			        final View dialogView = inflater.inflate(R.layout.add_class_layout, null);
			        // Inflate and set the layout for the dialog  
			        builder.setView(dialogView);
			        builder.setTitle("Add a Class");
			        builder.setCancelable(false);
			        
			        builder.setPositiveButton("Add",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, add a class
							EditText className = (EditText) dialogView.findViewById(R.id.className);
							
							if(className.length() > 0){
								Toast.makeText(getActivity(), "Add classes", Toast.LENGTH_SHORT).show();
								
								// get random color
								int nameLength = className.getText().length();
								int arrayLength = colorArray.length;
								int color = colorArray[arrayLength%nameLength];
								
								//add the class to the list
								Class newClass = new Class(className.getText().toString(), color);
								classList.add(newClass);
								
								//add the class to the db
								
							}else{
								Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						}
					  });
			        
					builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							dialog.cancel();
						}
					});
	 		        
			        // create alert dialog
					AlertDialog popup = builder.create();
					popup.show();
					
		        	return true;
			    }
				return true;
			}
		});
		
		return ret;
	}
	
}
