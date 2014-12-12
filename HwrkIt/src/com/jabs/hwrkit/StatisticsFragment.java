package com.jabs.hwrkit;


import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.jabs.globals.User;
import com.jabs.structures.HwrkTime;
import com.jabs.structures.Class;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class StatisticsFragment extends Fragment{
	public StatisticsFragment() {

	}

    // This should be changed for the user to customize it. Need to add to DB
    float SUGGESTED_HOURS = 3 * 60 * 60; //Suggested amount of hours per course (course outline), in seconds
    HashMap<Integer, ArrayList<HwrkTime>> allClasses = new HashMap<Integer, ArrayList<HwrkTime>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ret = inflater.inflate(R.layout.fragment_statistics, container,
				false);
        final PieGraph pg = (PieGraph) ret.findViewById(R.id.graph);
        final LineGraph li = (LineGraph)ret.findViewById(R.id.lineGraph);
        final ArrayList<String> colors = new ArrayList(); //create some colors for pie chart
        colors.add("#E65100");
        colors.add("#F57C00");
        colors.add("#EF6C00");
        colors.add("#FB8C00");
        colors.add("#FFE0B2");
        final String NO_COLOR = "#C0C0C0"; //Where no data is on chart
        User theUser = User.getInstance();

        Spinner spinner = (Spinner) ret.findViewById(R.id.classSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Bundle args = this.getArguments();
        double[] stats = new double[3];
        // to check if bundle was sent or not
        stats[0] = -1.0;

        // populate spinner with classnames, create light statistics data
        ArrayList<Class> hisClasses = theUser.getClasses();
        for(int i = 0; i < hisClasses.size(); i++){
            spinnerAdapter.add(hisClasses.get(i).getClassName());
            allClasses.put(i, hisClasses.get(i).getTimes());  // get all times for that class
        }

        //classesTimes.get(1).;
        spinnerAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<HwrkTime> classTimes = allClasses.get(position);

                //PIE CHART
                pg.removeSlices();
                li.removeAllLines();
                int i = 0;
                float total = 0;
                Line l = new Line();

                for (HwrkTime time : classTimes) {
                    PieSlice slice = new PieSlice();
                    slice.setColor(Color.parseColor(colors.get(i%colors.size()))); //get next color
                    float percentage = (float) time.getLengthSeconds() / SUGGESTED_HOURS; //percentage completed of those hours
                    total+=percentage;
                    System.out.println("Time "+new Integer(i).toString()+": "+time.getFormattedTimeSpent());
                    slice.setValue(percentage);
                    pg.addSlice(slice);

                    // LINE GRAPH

                    LinePoint p = new LinePoint();
                    p.setX(i);
                    p.setY(time.getLengthSeconds()/60); //Minutes
                    l.addPoint(p);
                    i++;
                }
                l.setColor(Color.parseColor("#FFBB33"));
                li.addLine(l);
                li.setRangeX(0, li.getMaxX());
                System.out.print(li.getMaxY()+10);

                li.setLineToFill(0);
                if (total<1){
                    li.setRangeY(0, SUGGESTED_HOURS/60);
                }
                else{
                    li.setRangeY(0,li.getMaxY()+10);
                }

                TextView txtPercentage = (TextView) ret.findViewById(R.id.percentage);
                txtPercentage.setText(new Integer((int)(total*100)).toString()+"% Completion");
                if (total < 1){
                    PieSlice slice = new PieSlice();
                    slice.setColor(Color.parseColor(NO_COLOR)); //get next color
                    slice.setValue(1-total);
                    pg.addSlice(slice);
                }
                pg.setInnerCircleRatio(200);
                pg.setPadding(2);
                for (PieSlice s : pg.getSlices()){
                    float old = s.getValue();
                    s.setValue(5);
                    s.setGoalValue(old);
                }
                pg.setDuration(1000);//default if unspecified is 300 ms
                pg.setInterpolator(new AccelerateDecelerateInterpolator());//default if unspecified is linear; constant speed
                pg.animateToGoalValues();


            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
		
		// bundle wasn't sent
		if (stats[0] == -1.0){
			stats[0] = 30.0;
			stats[1] = 10.0;
			stats[2] = 60.0;
		}





		return ret;
	}
}
