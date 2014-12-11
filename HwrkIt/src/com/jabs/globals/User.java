package com.jabs.globals;

import com.jabs.tasks.asyncLogin;
import com.jabs.tasks.asyncRegister;

import android.content.Context;

public class User {
	private static User instance;
	
	public static Void Init(Context context, String email, String password){
		instance = new User();
		return checkLogin(email, password, context);
	}
	
	private String email;
	private String name;
	private char userType;
	
	// Get the instance, use the functions on the specific instance
	public static User getInstance(){
		return instance;
	}
	
	public User(){
		// Do nothing for now
	}
	
	public static Void checkLogin(String email, String password, Context theContext){
		// Check the login here and fetch the results if it's good
		String url = "http://linux2-cs.johnabbott.qc.ca:30000/users/search/findByEmail?email="+email;
		// Async request
		asyncLogin doLogin = new asyncLogin(email, password, theContext);
		doLogin.execute(url);
		return null;
	}
	
	public static Void registerUser(String email, String password, String firstName, String lastName){
		String url = "http://linux2-cs.johnabbott.qc.ca:30000/users";
		// Async request
		asyncRegister doRegister = new asyncRegister(email, password, firstName, lastName);
		doRegister.execute(url);
		return null;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getName(){
		return this.name;
	}
	
	public char getUserType(){
		return this.userType;
	}
}
