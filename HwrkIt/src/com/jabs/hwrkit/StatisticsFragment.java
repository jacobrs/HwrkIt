package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;

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
		slice.setValue(7);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#D3D3D3"));
		slice.setValue(3);
		pg.addSlice(slice);
		
		pg.setInnerCircleRatio(200);
		pg.setPadding(5);
		for (PieSlice s : pg.getSlices())
            s.setGoalValue((float)Math.random() * 10);
        pg.setDuration(1000);//default if unspecified is 300 ms
        pg.setInterpolator(new AccelerateDecelerateInterpolator());//default if unspecified is linear; constant speed
        pg.animateToGoalValues();
		return ret;
	}
}
