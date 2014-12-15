package com.jabs.hwrkit;

import java.util.LinkedList;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.adapters.HwrkAdapter;
import com.jabs.globals.User;
import com.jabs.structures.HwrkTime;
import com.jabs.structures.Class;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jabs.tasks.asyncGetTimes;
import com.jabs.time.RadialPickerLayout;
import com.jabs.time.TimePickerDialog;

;

public class HwrkFragment extends Fragment {
	public LinkedList<HwrkTime> times = null;
	private int startHour = 0;
	private int startMinute = 0;
	private int endHour = 0;
	private int endMinute = 0;
	private View v;
	private Context context;
	
	public HwrkFragment() {
		times = new LinkedList<HwrkTime>();
		Class tmp = new Class("Geometry");
		Class tmp2 = new Class("Astrophysics");
		//times.add(new HwrkTime("18-11-14 12:30:00", "18-11-14 1:30:00", tmp));
		//times.add(new HwrkTime("18-11-14 5:00:00", "18-11-14 7:30:00", tmp2));
		//times.add(new HwrkTime("18-11-14 8:30:00", "18-11-14 9:00:00", tmp));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View ret = inflater.inflate(R.layout.fragment_hwrk, container, false);

		load(ret, this.getActivity());
		return ret;
	}

	public void load(View v, Context context) {
		this.v = v;
		this.context = context;
		asyncGetTimes gt = new asyncGetTimes(User.getInstance().getID(), context, this);
		gt.execute();
		final Button btn = (Button) v.findViewById(R.id.addhwrk);
		btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					// set drawable
				} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					EndGetter clb = new EndGetter();
					Toast.makeText(getActivity(), "Add Start Time",
							Toast.LENGTH_SHORT).show();
					android.app.FragmentManager fm = getActivity()
							.getFragmentManager();
					DialogFragment fragment = TimePickerDialog.newInstance(
							clb, 12, 0, true);
					fragment.show(fm, "Start Time");
					// Toast.makeText(getActivity(), "Add homework popup",
					// Toast.LENGTH_SHORT).show();
					// return true;
				}
				return true;
			}
		});
	
	}
	
	public void setListView(){
		ListView list = (ListView) this.v.findViewById(R.id.hwrkList);
		HwrkAdapter adapter = new HwrkAdapter(this.context, this.times);
		if(list != null){
			list.setAdapter(adapter);
		}
	}

	public class EndGetter implements TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			FinishInput clb = new FinishInput();
			startHour = hourOfDay;
			startMinute = minute;
			Toast.makeText(getActivity(), "Add End Time",
					Toast.LENGTH_SHORT).show();
			android.app.FragmentManager fm = getActivity()
					.getFragmentManager();
			DialogFragment fragment = TimePickerDialog.newInstance(
					clb, startHour, (startMinute + 30) % 60, true);
			fragment.show(fm, "End Time?");
		}

	}
	
	public class FinishInput implements TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			Toast.makeText(
					getActivity(),
					"Added with: " + hourOfDay + " hours and " + minute
							+ " minutes", Toast.LENGTH_SHORT).show();
		}

	}
}
