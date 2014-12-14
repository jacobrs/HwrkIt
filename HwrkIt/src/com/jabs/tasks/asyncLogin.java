package com.jabs.tasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jabs.globals.BCrypt;
import com.jabs.globals.User;
import com.jabs.hwrkit.LoginFrontFragment;
import com.jabs.hwrkit.MainActivity;
import com.jabs.structures.Class;
import com.jabs.structures.HwrkTime;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 11th, 2014
 * 
 * @purpose:
 * 
 * 		This class is used to log a user in, it does error handling
 * on the appropriate activity. It will start the main activity of
 * the application after it is done verifying that everything is okay.
 * If there was an error with the login it will notify the user. 
 * This class also creates an ArrayList of classes and adds it to a
 * singleton so that it is easier to access all of the user's information.
 * 
 ****************************************************************/
public class asyncLogin extends AsyncTask<String, Void, Void> {
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String errorText;
	private Boolean success;
	private Context prevCont;
	private char userType;
	private final Intent mainActivity;
	
	// Prepare everything that we need, we need the context because
	// we are creating with an intent with the purpose of starting
	// a new activity (potentially) later on
	public asyncLogin(String email, String password, Context prevCont) {
		// Assume failure until we know the password matches
		// and we have received all of the class information
		this.success = false;
		this.errorText = "";
		this.email = email;
		// Previous caller's context
		this.prevCont = prevCont;
		// Intent used to start a new activity remotely (from this task)
		this.mainActivity = new Intent(prevCont, MainActivity.class);
		this.mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.password = password;
	}

	// Get the user information in the background and make sure
	// the password matches the user's
	@Override
	protected Void doInBackground(String... params) {
		URL url;
		String href = "";
		try {
			// Create the GET request
			url = new URL("http://linux2-cs.johnabbott.qc.ca:30000/users/search/findByEmail?email="+email);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			con.setDoInput(true);
			con.setChunkedStreamingMode(0);
		
			if(con.getResponseCode() == 200){
				// Get the user's information
				Scanner scanner = new Scanner(con.getInputStream()).useDelimiter("\\A");
				String json = scanner.hasNext() ? scanner.next() : "";
				
				// Parse the JSON and get all the appropriate information
				JSONTokener tokener = new JSONTokener(json);
				try {	
					JSONObject root = (JSONObject) tokener.nextValue();
					// 
					if(root.has("_embedded")){					
						JSONObject realRoot = root.getJSONObject("_embedded");
						JSONArray res = realRoot.getJSONArray("users");
						
						JSONObject user = res.getJSONObject(0);
						String password = user.getString("password");
						firstName = user.getString("firstName");
						lastName = user.getString("lastName");
						userType = user.getString("userType").charAt(0);
						// Get the user's classes
						JSONObject linksRoot = user.getJSONObject("_links");
						JSONObject linksSelf = linksRoot.getJSONObject("self");
						// Link to all of this user's classes
						href = linksSelf.getString("href");
						String[] splitHref = href.split("/");
						href = splitHref[splitHref.length-1];
						// Check if the user supplied a proper password
						if(BCrypt.checkpw(this.password, password)){
							success = true;
						}else{
							// User entered incorrect password
							errorText = "Email or password is incorrect";
						}
					}else{
						// User probably does not exist
						errorText = "Email or password is incorrect";
					}
				}catch (JSONException e){
					// shouldn't really happen
					errorText = "Parsing failed";
				}
			}else{
				errorText = "Connection could not be established, check your network connection";
			}
		}catch (MalformedURLException e){
			// Will happen very rarely unless the user is trying something malicious or is using
			// characters that the url cannot comprehend
			errorText = "Something went wrong with the request";
		}catch (IOException e){
			// Something went wrong with getting the input stream, probably a network error
			errorText = "Connection could not be established, check your network connection";
		}
		
		if(success){
			success = false;
			// Create all the classes and store them in the user
			String link = "http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/api/getUsersClasses.php?uid="+href;
			try {
				// Set up the get request
				url = new URL(link);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setDoInput(true);
				con.setChunkedStreamingMode(0);
				
				if(con.getResponseCode() == 200){
					Scanner scanner = new Scanner(con.getInputStream()).useDelimiter("\\A");
					String json = scanner.hasNext() ? scanner.next() : "";
					
					// Parse all the information and place it into our singleton
					JSONTokener tokener = new JSONTokener(json);
					try {	
						JSONObject root = (JSONObject) tokener.nextValue();
						
						// Create the container for all the classes (this will
						// be added to the singleton)
						ArrayList<Class> allTheClasses = new ArrayList<Class>();
						
						// Create all the classes
						JSONArray classes = root.getJSONArray("classes");
						for(int i = 0; i < classes.length(); i++){
							JSONObject theClass = classes.getJSONObject(i);
							int teacherID = theClass.getInt("TID");
							int classID = theClass.getInt("CID");
							String className = theClass.getString("Name");
							
							// Create a temporary class and the container for all
							// the times that belong to this class
							Class tempClass = new Class(className);
							ArrayList<HwrkTime> allTheTimes = new ArrayList<HwrkTime>();
							
							// Get all the times for this particular class
							JSONArray times = theClass.getJSONArray("times");
							for(int j = 0; j < times.length(); j++){
								JSONObject theTime = times.getJSONObject(j);
								String endTimeString = theTime.getString("endTime");
								String startTimeString = theTime.getString("startTime");
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.ENGLISH);
								
								int timeID = theTime.getInt("TID");
								Date endDate;
								Date startDate;
								try {
									endDate = format.parse(endTimeString);
									startDate = format.parse(startTimeString);
									HwrkTime tempTime = new HwrkTime(startDate, endDate);
									// Add this particular time to our container
									allTheTimes.add(tempTime);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// Store the times into our class
							tempClass.setTimes(allTheTimes);
							// Add our class to the container
							allTheClasses.add(tempClass);
						}
						// Add all the classes to the user
						User mainUser = User.getInstance();
						mainUser.setClasses(allTheClasses);
						// Add other information too
						mainUser.setEmail(email);
						mainUser.setName(firstName, lastName);
						mainUser.setUserType(userType);
						// Everything succeeded
						success = true;
					}catch (JSONException e){
						// shouldn't really happen
						errorText = "Parsing failed";
					}
				}else{
					// Network error
					errorText = "Connection could not be established, check your network connection";
				}
			}catch (MalformedURLException e){
				// Will happen very rarely unless the user is trying something malicious or is using
				// characters that the url cannot comprehend
				errorText = "Something went wrong with the request";
			}catch (IOException e){
				// Something went wrong with getting the input stream, probably a network error
				errorText = "Connection could not be established, check your network connection";
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		LoginFrontFragment.setBtnText("Login");
		// Bring the user to the new activity on success
		if(success){
			this.prevCont.startActivity(mainActivity);
		}else{
			// warn the user with a specific error
			LoginFrontFragment.displayErrors(errorText);
		}
		return;
	}
}