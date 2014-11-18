package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.structures.Class;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		final List<Class> classList = new ArrayList<Class>();
		classList.add(new Class("English"));
		classList.add(new Class("French"));
		classList.add(new Class("Math"));
		classList.add(new Class("Humanities"));
		classList.add(new Class("Gym"));
		classList.add(new Class("Physics"));
		
		ListView list = (ListView) ret.findViewById(R.id.list);
		
		ClassesAdapter adapter = new ClassesAdapter(this, classList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast toast = Toast.makeText(getActivity(),
						classList.get(position).getClassName().toString(), Toast.LENGTH_SHORT);
						toast.show();
						
				Bundle passClass = new Bundle();
				passClass.putString("className", classList.get(position).getClassName());
				// pass stats
				
				double stat1 = Math.random();
				double stat2 = Math.random();
				double stat3 = Math.random();
				double[] stats = {stat1, stat2, stat3};
				passClass.putDoubleArray("stats", stats);
				
				StatisticsFragment classStats = new StatisticsFragment();
				classStats.setArguments(passClass);
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						classStats).commit();
				
			}
			
		});
		return ret;
	}
}
