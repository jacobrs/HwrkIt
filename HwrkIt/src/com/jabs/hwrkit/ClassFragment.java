package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.structures.Course;

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
		final List<Course> classList = new ArrayList<Course>();
		classList.add(new Course("English"));
		classList.add(new Course("French"));
		classList.add(new Course("Math"));
		classList.add(new Course("Humanities"));
		classList.add(new Course("Gym"));
		classList.add(new Course("Physics"));
		
		ListView list = (ListView) ret.findViewById(R.id.classList);
		
		ClassesAdapter adapter = new ClassesAdapter(this.getActivity(), classList);
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
				
				int Min = 0;
				int Max = 100;
				
				double[] stats = new double[3];
				for(int i = 0; i < 3; i++){
					double stat = randomIntegerGenerator(Min, Max);
					Max -= stat;
					stats[i] = stat;
				}
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
	
	public int randomIntegerGenerator(int Min, int Max){
		return Min+(int)(Math.random()*((Max-Min) + 1));
	}
}
