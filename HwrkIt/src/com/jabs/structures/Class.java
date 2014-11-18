package com.jabs.structures;

public class Class {
	private String _className;
	private int _resid;
	// add students maybe
	
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
	
	
}
