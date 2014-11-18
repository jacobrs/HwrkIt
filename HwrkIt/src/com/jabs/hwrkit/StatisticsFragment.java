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

public class StatisticsFragment extends Fragment{
	public StatisticsFragment() {
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_statistics, container,
				false);
		
		PieGraph pg = (PieGraph)ret.findViewById(R.id.graph);
		PieSlice slice = new PieSlice();
		slice.setColor(Color.parseColor("#99CC00"));
		slice.setValue(50);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#D3D3D3"));
		slice.setValue(50);
		pg.addSlice(slice);
		
		pg.setInnerCircleRatio(200);
		pg.setPadding(5);
		for (PieSlice s : pg.getSlices())
            s.setGoalValue((float)Math.random() * 10);
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
