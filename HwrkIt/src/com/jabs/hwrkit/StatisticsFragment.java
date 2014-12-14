package com.jabs.hwrkit;


import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.jabs.globals.User;
import com.jabs.structures.HwrkTime;
import com.jabs.structures.Class;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    private int clickedPosition;
    private String NO_COLOR = "#C0C0C0"; //Where no data is on chart
    
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

        // get position of class clicked in listview
        Bundle args = this.getArguments();
        if(args != null){
        	if(args.containsKey("classPosition")){
        		clickedPosition = args.getInt("classPosition");
        		// set the spinner to the selected class
        		spinner.setSelection(clickedPosition, true);
        		// calcuate times and animate graphs
        		setSpinnerData(ret, colors, clickedPosition);
        	}
        }
        
        //classesTimes.get(1).;
        spinnerAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSpinnerData(ret, colors, position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

		return ret;
	}
	
	public void setSpinnerData(View ret, ArrayList<String> colors, int position){
		 ArrayList<HwrkTime> classTimes = allClasses.get(position);
         PieGraph pg = (PieGraph) ret.findViewById(R.id.graph);
         BarGraph barGraph = (BarGraph)ret.findViewById(R.id.barGraph);
         TextView avgWeek = (TextView) ret.findViewById(R.id.avgWeek);
         TextView totWeek = (TextView) ret.findViewById(R.id.totWeek);
         TextView avgDay = (TextView) ret.findViewById(R.id.avgDay);
         ImageView status = (ImageView) ret.findViewById(R.id.status);


         //PIE CHART
         pg.removeSlices();
         int i = 0;
         float total = 0;
         classTimes = getTimesOverRange(classTimes, 7); //get class times in last 7 days
         ArrayList<Bar> points = new ArrayList<Bar>();
         for (HwrkTime time : classTimes) {
             //Pie chart
             PieSlice slice = new PieSlice();
             slice.setColor(Color.parseColor(colors.get(i%colors.size()))); //get next color
             float percentage = (float) time.getLengthSeconds() / SUGGESTED_HOURS; //percentage completed of those hours
             total+=percentage;
             slice.setValue(percentage);
             pg.addSlice(slice);

             // BAR GRAPH

             Bar d = new Bar();
             d.setColor(Color.parseColor(colors.get(i % colors.size())));
             d.setName(time.getFormattedTimeSpent());
             d.setValue(time.getLengthSeconds() /60); //minutes
             points.add(d);

             i++;
         }
         barGraph.setBars(points);

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
         Float weekBeforeLast = getTimesTotal(allClasses.get(position),14)/SUGGESTED_HOURS - total;
         if (total > weekBeforeLast){
             status.setImageResource(R.drawable.ic_trending_up_24px);
         }
         else if (total < weekBeforeLast){
             status.setImageResource(R.drawable.ic_trending_down_24px);
         }
         else{
             status.setImageResource(R.drawable.ic_trending_neutral_24px);
         }

         //Avg week
         if (classTimes.size()!=0) {
             avgWeek.setText(String.format("%.1f", (getTimesTotal(classTimes, 7) / classTimes.size() / 60)) + " average minutes this week");
             totWeek.setText(String.format("%.1f", (getTimesTotal(classTimes, 7) / 60)) + " total minutes this week");
         }else {
             avgWeek.setText("0 average minutes this week");
             totWeek.setText("0 total minutes this week");
         }
         ArrayList<HwrkTime> day = getTimesOverRange(classTimes, 1);
         if (day.size() !=0)
            avgDay.setText(String.format("%.1f", (getTimesTotal(day,1) / day.size() / 60)) + " average minutes today");
         else
            avgDay.setText("0 average minutes today");
	}
	
    //get the total amount of time spent over a duration, in seconds
    float getTimesTotal(ArrayList<HwrkTime> times, int duration) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        float total = 0;
        Date dateRange = new Date((System.currentTimeMillis() - (duration * DAY_IN_MS)));
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
        for (HwrkTime time: times){
            if (time.getStartTime().getTime() > dateRange.getTime()){
                rangeTimes.add(time);
            }
        }
        return rangeTimes;
    }

}
