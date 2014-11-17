package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.structures.Class;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ClassFragment extends Fragment {
	public ClassFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_classes, container,
				false);
		List<Class> classList = new ArrayList<Class>();
		classList.add(new Class("English"));
		classList.add(new Class("French"));
		classList.add(new Class("Math"));
		classList.add(new Class("Humanities"));
		classList.add(new Class("Gym"));
		classList.add(new Class("Physics"));
		
		ListView list = (ListView) ret.findViewById(R.id.list);
		
		ClassesAdapter adapter = new ClassesAdapter(this, classList);
		list.setAdapter(adapter);
		return ret;
	}
}
