package com.jabs.structures;

import com.jabs.structures.Course;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HwrkTime {
	private Date startTime;
	private Date endTime;
	private Course parent;
	
	public HwrkTime(Date startTime, Date endTime, Course parent){
		this.startTime = startTime;
		this.endTime = endTime;
		this.parent = parent;
	}
	
	public HwrkTime(String start, String end, Course parent){
		try {
			startTime = new SimpleDateFormat("d/M/y h:m:s", Locale.ENGLISH).parse(start);
			endTime = new SimpleDateFormat("d/M/y h:m:s", Locale.ENGLISH).parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			startTime = null;
			endTime = null;
			e.printStackTrace();
		}
		this.parent = parent;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getLengthSeconds(){
		return endTime.getTime() - startTime.getTime();
	}
	
	public Course getCourse(){
		return this.parent;
	}
}