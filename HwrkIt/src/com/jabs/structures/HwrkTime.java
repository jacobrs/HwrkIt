package com.jabs.structures;

import com.jabs.structures.Class;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HwrkTime {
	private Date startTime;
	private Date endTime;
	private Class parent;
	private String desc;
	
	public HwrkTime(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public HwrkTime(String start, String end, Class parent, String desc){
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
		this.desc = desc;
		if(desc == ""){
			this.desc = "No details inputed";
		}
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
		return (endTime.getTime() - startTime.getTime()) / 1000;
	}
	
	public String getFormattedTimeSpent(){
		Long timespent = getLengthSeconds();
		
		Long hours = (timespent - (timespent % 3600)) / 3600; timespent -= hours * 3600;
		Long mins  = (timespent - (timespent % 60)) / 60; timespent -= mins * 60;
		Long seconds = timespent;
		String ret = "";
		if(hours > 0 || mins > 0 || seconds > 0){
			boolean more = false;
			if(hours > 0){
				String add = (hours > 1)?"s":"";
				ret += hours.toString() + " hour"+add;
				more = true;
			}
			if(mins > 0){
				String add = (mins > 1)?"s":"";
				if(seconds > 0 && more){
					ret += " and " + mins.toString() + " minute"+add;
				}else{
					ret += mins.toString() + " minute"+add;
				}
				more = true;
			}
			if(seconds > 0){
				String add = (seconds > 1)?"s":"";
				if(more){
					ret += " and " + seconds.toString() + "second"+add;
				}
			}
		}else{
			ret = "no time spent";
		}
		return ret;
	}
	
	public Class getCourse(){
		return parent;
	}
	
	public String getDesc(){
		return this.desc;
	}
}
