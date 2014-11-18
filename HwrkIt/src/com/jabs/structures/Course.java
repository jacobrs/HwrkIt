package com.jabs.structures;

public class Course {
	private String _className;
	private int _resid;
	// add students maybe
	
	public Course(String name){
		this._className = name;
	}
	
	public Course(String name, int artId){
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
	
	
}
