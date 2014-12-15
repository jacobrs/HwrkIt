package com.jabs.structures;

import java.util.ArrayList;

public class Class {
	private String _className;
	private int _color;
	private int _classid;
	private ArrayList<HwrkTime> times = new ArrayList<HwrkTime>();
	// add students maybe
	// and maybe also the teacher
	
	public Class(int classid, String name){
		this._className = name;
		this._color = 0;
		this._classid = classid;
	}
	
	public Class(String name, int artId){
		this._className = name;
		this._color = artId;
	}

	public String getClassName() {
		return this._className;
	}

	public void setClassName(String name) {
		this._className = name;
	}
	
	public int getColor() {
		return this._color;
	}

	public void setColor(int color) {
		this._color = color;
	}
	
	public void setTimes(ArrayList<HwrkTime> times){
		this.times = times;
	}
	
	public ArrayList<HwrkTime> getTimes(){
		return this.times;
	}
	
	public int getClassID(){
		return this._classid;
	}
	
}
