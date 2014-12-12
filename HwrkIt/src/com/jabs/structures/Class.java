package com.jabs.structures;

import java.util.ArrayList;

public class Class {
	private String _className;
	private int _resid;
	private ArrayList<HwrkTime> times = new ArrayList<HwrkTime>();
	// add students maybe
	// and maybe also the teacher
	
	public Class(String name){
		this._className = name;
	}
	
	public Class(String name, int artId){
		this._className = name;
		this._resid = artId;
	}

	public String getClassName() {
		return this._className;
	}

	public void setClassName(String name) {
		this._className = name;
	}

	public int getResId() {
		return this._resid;
	}

	public void setResId(int resid) {
		this._resid = resid;
	}
	
	public void setTimes(ArrayList<HwrkTime> times){
		this.times = times;
	}
	
	public ArrayList<HwrkTime> getTimes(){
		return this.times;
	}
	
}
