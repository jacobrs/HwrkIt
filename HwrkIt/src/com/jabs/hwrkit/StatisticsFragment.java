package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.jabs.adapters.ClassesAdapter;
import com.jabs.structures.Class;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class StatisticsFragment extends Fragment{
	public StatisticsFragment() {
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_statistics, container,
				false);
		
		Bundle args = this.getArguments();
		double[] stats = new double[3];
		// to check if bundle was sent or not
		stats[0] = -1.0;
		String className = "";
		if(args != null){
			if(args.containsKey("className")){
				className = args.getString("className");
			}
			if(args.containsKey("stats")){
				stats = args.getDoubleArray("stats");
			}
		}
		
		// bundle wasn't sent
		if (stats[0] == -1.0){
			stats[0] = 30.0;
			stats[1] = 10.0;
			stats[2] = 60.0;
		}
		
		PieGraph pg = (PieGraph)ret.findViewById(R.id.graph);
		PieSlice slice = new PieSlice();
		slice.setColor(Color.parseColor("#99CC00"));
		slice.setValue((float) stats[0]);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#D3D3D3"));
		slice.setValue((float) stats[1]);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#FFFFFF"));
		slice.setValue((float) stats[2]);
		pg.addSlice(slice);
		
		Double tmp = stats[0];
		Toast toast = Toast.makeText(getActivity(),
				tmp.toString(), Toast.LENGTH_SHORT);
				toast.show();
		
		pg.setInnerCircleRatio(200);
		pg.setPadding(2);
		for (PieSlice s : pg.getSlices()){
			float old = s.getValue();
			s.setValue((float)Math.random() * 10);
            s.setGoalValue(old);
		} 
        pg.setDuration(1000);//default if unspecified is 300 ms
        pg.setInterpolator(new AccelerateDecelerateInterpolator());//default if unspecified is linear; constant speed
        pg.animateToGoalValues();
        
        Spinner spinner = (Spinner) ret.findViewById(R.id.classSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity() ,
             R.array.class_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     	// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        
        Line l = new Line();
        LinePoint p = new LinePoint();
        p.setX(0);
        p.setY(5);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(8);
        p.setY(8);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(10);
        p.setY(4);
        l.addPoint(p);
        l.setColor(Color.parseColor("#FFBB33"));

        LineGraph li = (LineGraph)ret.findViewById(R.id.lineGraph);
        li.addLine(l);
        li.setRangeY(0, 10);
        li.setLineToFill(0);
		return ret;
	}
}
