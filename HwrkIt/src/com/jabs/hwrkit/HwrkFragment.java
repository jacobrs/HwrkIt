package com.jabs.hwrkit;

import java.util.LinkedList;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.adapters.HwrkAdapter;
import com.jabs.structures.HwrkTime;
import com.jabs.structures.Class;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HwrkFragment extends Fragment {
	private LinkedList<HwrkTime> times = null;
	
	public HwrkFragment() {
		times = new LinkedList<HwrkTime>();
		Class tmp = new Class("Geometry");
		Class tmp2 = new Class("Astrophysics");
		times.add(new HwrkTime("18/11/14 12:30:00", "18/11/14 1:30:00", tmp, "Test description"));
		times.add(new HwrkTime("18/11/14 6:30:00", "18/11/14 7:30:00", tmp2, "I'm going to mars :D"));
		times.add(new HwrkTime("18/11/14 8:30:00", "18/11/14 9:00:00", tmp, ""));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View ret = inflater.inflate(R.layout.fragment_hwrk, container,
				false);
		
		load(ret, this.getActivity());
		return ret;
	}
	
	public void load(View v, Context context){
		ListView list = (ListView) v.findViewById(R.id.hwrkList);
		final Button btn  = (Button) v.findViewById(R.id.addhwrk);
		btn.setOnTouchListener(new OnTouchListener () {
			  	@Override
				public boolean onTouch(final View view, final MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					// set drawable
			    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
			    	Toast.makeText(getActivity(), "Add homework popup", Toast.LENGTH_SHORT).show();		
		        	return true;
			    }
				return true;
			}
		});
		HwrkAdapter adapter = new HwrkAdapter(context, this.times);
		list.setAdapter(adapter);
	}
}
