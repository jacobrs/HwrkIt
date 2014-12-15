package com.jabs.hwrkit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import com.jabs.adapters.ClassOptionAdapter;
import com.jabs.adapters.ClassesAdapter;
import com.jabs.adapters.HwrkAdapter;
import com.jabs.globals.User;
import com.jabs.structures.HwrkTime;
import com.jabs.structures.Class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jabs.tasks.asyncAddTime;
import com.jabs.tasks.asyncGetTimes;
import com.jabs.time.RadialPickerLayout;
import com.jabs.time.TimePickerDialog;

;

public class HwrkFragment extends Fragment {
	public LinkedList<HwrkTime> times = null;
	private int startHour = 0;
	private HwrkFragment ref = this;
	private int startMinute = 0;
	private int endHour = 0;
	private int endMinute = 0;
	private View v;
	private Context context;
	public Dialog classDialog = null;
	private Class addingClass = null;
	
	public HwrkFragment() {
		times = new LinkedList<HwrkTime>();
		//Class tmp = new Class("Geometry");
		//Class tmp2 = new Class("Astrophysics");
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
		fetchTimes(context);
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
	
	public void fetchTimes(Context context){
		asyncGetTimes gt = new asyncGetTimes(User.getInstance().getID(), context, this);
		gt.execute();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Select Class");
			endHour = hourOfDay;
			endMinute = minute;
			ListView modeList = new ListView(context);
			ClassOptionAdapter modeAdapter = new ClassOptionAdapter(context, User.getInstance().getClasses(), ref, classDialog);
			modeList.setAdapter(modeAdapter);
			modeList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
							
					((ClassOptionAdapter) parent.getAdapter()).closeModal();
					addingClass = (Class) parent.getAdapter().getItem(position);
					Log.d("Passing?", addingClass.getClassID()+"");
					submitTime();
					
				}
				
			});
			builder.setView(modeList);
			classDialog = builder.create();

			classDialog.show();
		}

	}
	
	private void submitTime(){
		// make sure the user chose a class
		if(addingClass != null){
			// now we need to add the time
			Calendar c = Calendar.getInstance(); 
			Date today = c.getTime();
			String start = new SimpleDateFormat("y-M-d", Locale.ENGLISH).format(today);
			
			/* check if you have to switch end time to start time because its smaller */
			if((endHour * 60 + endMinute) < (startHour * 60 + startMinute)){
				int tmp = endHour;
				endHour = startHour;
				startHour = tmp;
				tmp = endMinute;
				endMinute = startMinute;
				startMinute = tmp;
			}
			
			String end = start + " "+endHour+":"+endMinute+":00";
			start += " "+startHour+":"+startMinute+":00";
			
			HwrkTime timeToAdd = new HwrkTime(start, end, this.addingClass);
			asyncAddTime ad = new asyncAddTime(User.getInstance().getID(), this.context, this, timeToAdd, addingClass);

			ad.execute();
		}
	}
}
