package com.jabs.globals;

import java.util.ArrayList;

import com.jabs.tasks.asyncLogin;
import com.jabs.tasks.asyncRegister;

import android.content.Context;
import com.jabs.structures.Class;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 11th, 2014
 * 
 * @purpose:
 * 
 * 		This class is a singleton that will be used accross the
 * entire platform, it is built on login and if some static
 * variables become null (persistance will handle this)
 * 
 ****************************************************************/
public class User {
	private static User instance;
	
	// Initialize a new instance of the user and check if
	// the information given is correct
	public static Void Init(Context context, String email, String password){
		instance = new User();
		return checkLogin(email, password, context);
	}
	
	public User(){
		// Do nothing for now
	}
	
	// Get the instance, use the functions on the specific instance
	public static User getInstance(){
		return instance;
	}
	
	// Asynchronously log a user in, the context is needed to start a new activity
	public static Void checkLogin(String email, String password, Context theContext){
		// Check the login here and fetch the results if it's good
		String url = "http://linux2-cs.johnabbott.qc.ca:30000/users/search/findByEmail?email="+email;
		// Async request
		asyncLogin doLogin = new asyncLogin(email, password, theContext);
		doLogin.execute(url);
		return null;
	}
	
	// Asynchronously register a user
	public static Void registerUser(String email, String password, String firstName, String lastName){
		String url = "http://linux2-cs.johnabbott.qc.ca:30000/users";
		// Async request
		asyncRegister doRegister = new asyncRegister(email, password, firstName, lastName);
		doRegister.execute(url);
		return null;
	}
	
	// Body of the User class
	private String email;
	private String name;
	private char userType;
	private ArrayList<Class> usersClasses = new ArrayList<Class>();
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public void setName(String firstName, String lastName){
		this.name = firstName + " " + lastName;
	}
	
	public void setUserType(char userType){
		this.userType = userType;
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
	
	public void setClasses(ArrayList<Class> allTheClasses){
		this.usersClasses = allTheClasses;
	}
	
	public ArrayList<Class> getClasses(){
		return this.usersClasses;
	}
}
