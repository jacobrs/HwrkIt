package com.jabs.hwrkit;

import java.util.LinkedList;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.adapters.HwrkAdapter;
import com.jabs.structures.Course;
import com.jabs.structures.HwrkTime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HwrkFragment extends Fragment {
	private LinkedList<HwrkTime> times = null;
	public HwrkFragment() {
		Course tmp = new Course("Geometry");
		times.add(new HwrkTime("18/11/14 12:30:00", "18/11/14 1:30:00", tmp));
		times.add(new HwrkTime("18/11/14 6:30:00", "18/11/14 7:30:00", tmp));
		times.add(new HwrkTime("18/11/14 8:30:00", "18/11/14 9:00:00", tmp));
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
		ListView list = (ListView) v.findViewById(R.id.classList);
		
		HwrkAdapter adapter = new HwrkAdapter(context, this.times);
		list.setAdapter(adapter);
	}
}
