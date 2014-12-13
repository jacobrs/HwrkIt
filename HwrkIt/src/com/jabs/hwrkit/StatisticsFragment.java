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
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class StatisticsFragment extends Fragment{
	public StatisticsFragment() {

	}

    // This should be changed for the user to customize it. Need to add to DB
    float SUGGESTED_HOURS = 3 * 60 * 60; //Suggested amount of hours per course (course outline), in seconds
    private HashMap<Integer, ArrayList<HwrkTime>> allClasses = new HashMap<Integer, ArrayList<HwrkTime>>();
    private LinkedList<HwrkTime> hwrkTimes = new LinkedList<HwrkTime>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ret = inflater.inflate(R.layout.fragment_statistics, container,
				false);
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

        // populate spinner with classnames, create light statistics data
        final ArrayList<Class> hisClasses = theUser.getClasses();
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
                PieGraph pg = (PieGraph) ret.findViewById(R.id.graph);
                LineGraph li = (LineGraph)ret.findViewById(R.id.lineGraph);
                TextView avgWeek = (TextView) ret.findViewById(R.id.avgWeek);
                TextView totWeek = (TextView) ret.findViewById(R.id.totWeek);
                TextView avgDay = (TextView) ret.findViewById(R.id.avgDay);
                TextView txtMaxY = (TextView) ret.findViewById(R.id.txtMaxY);
                //PIE CHART
                pg.removeSlices();
                li.removeAllLines();
                int i = 0;
                float total = 0;
                Line l = new Line();
                LinePoint p = new LinePoint();
                p.setX(0);
                p.setY(0);
                l.addPoint(p);
                classTimes = getTimesOverRange(classTimes, 7); //get class times in last 7 days
                for (HwrkTime time : classTimes) {
                    //Pie chart
                    PieSlice slice = new PieSlice();
                    slice.setColor(Color.parseColor(colors.get(i%colors.size()))); //get next color
                    float percentage = (float) time.getLengthSeconds() / SUGGESTED_HOURS; //percentage completed of those hours
                    total+=percentage;
                    slice.setValue(percentage);
                    pg.addSlice(slice);

                    // LINE GRAPH

                    p = new LinePoint();
                    p.setX(i+1);
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
                    txtMaxY.setText(new Float(SUGGESTED_HOURS/60).toString());
                }
                else{
                    li.setRangeY(0,li.getMaxY()+10);
                    txtMaxY.setText(new Float(li.getMaxY()+10).toString());
                }

                TextView txtPercentage = (TextView) ret.findViewById(R.id.percentage);
                txtPercentage.setText(new Integer((int)(total*100)).toString()+"%");

                //Don't set animation on grey one
                for (PieSlice s : pg.getSlices()){
                    float old = s.getValue();
                    s.setValue(0);
                    s.setGoalValue(old);
                }

                if (total < 1){
                    PieSlice slice = new PieSlice();
                    slice.setColor(Color.parseColor(NO_COLOR)); //get next color
                    slice.setValue(1-total);
                    slice.setGoalValue(1-total);
                    pg.addSlice(slice);
                }
                pg.setInnerCircleRatio(200);
                pg.setPadding(2);

                pg.setDuration(1000);//default if unspecified is 300 ms
                pg.setInterpolator(new AccelerateDecelerateInterpolator());//default if unspecified is linear; constant speed
                pg.animateToGoalValues();

                // Process stats
                //Avg week
                if (classTimes.size()!=0) {
                    avgWeek.setText(new Float(getTimesTotal(classTimes, 7) / classTimes.size() / 60).toString() + " average minutes this week");
                    avgDay.setText(new Float(getTimesTotal(classTimes,1) / classTimes.size() / 60).toString() + " average minutes today");
                    totWeek.setText(new Float(getTimesTotal(classTimes, 7) / 60).toString() + " total minutes this week");
                }else {
                    avgWeek.setText("0 average minutes this week");
                    avgDay.setText("0 average minutes today");
                    totWeek.setText("0 total minutes this week");
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

		return ret;
	}

    //get the total amount of time spent over a duration, in seconds
    float getTimesTotal(ArrayList<HwrkTime> times, int duration) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        float total = 0;
        Date dateRange = new Date((System.currentTimeMillis() - (duration * DAY_IN_MS)));
        ArrayList<HwrkTime> theTimes = new ArrayList<HwrkTime>();
        for (HwrkTime time: times){
            if (time.getStartTime().getTime() > dateRange.getTime()){
                total += time.getLengthSeconds();
            }
        }
        return total;
    }
    //returns a list of times within time range
    ArrayList<HwrkTime> getTimesOverRange(ArrayList<HwrkTime> times, int duration){
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        ArrayList<HwrkTime> rangeTimes = new ArrayList<HwrkTime>();
        Date dateRange = new Date((System.currentTimeMillis() - (duration * DAY_IN_MS)));
        ArrayList<HwrkTime> theTimes = new ArrayList<HwrkTime>();
        for (HwrkTime time: times){
            if (time.getStartTime().getTime() > dateRange.getTime()){
                rangeTimes.add(time);
            }
        }
        return rangeTimes;
    }

}
