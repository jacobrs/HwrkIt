package com.jabs.tasks;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jabs.globals.BCrypt;
import com.jabs.globals.User;
import com.jabs.hwrkit.HwrkFragment;
import com.jabs.hwrkit.LoginFrontFragment;
import com.jabs.hwrkit.MainActivity;
import com.jabs.structures.Class;
import com.jabs.structures.HwrkTime;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class asyncAddTime extends AsyncTask<String, Void, Void> {
	private int userID;
	private String PORT = "30000";
	private String HOST = "linux2-cs.johnabbott.qc.ca";
	private String tmpclass;
	private String errorText;
	private Boolean success;
	private Context prevCont;
	private Class addClass;
	private HwrkTime addTime;
	private HwrkFragment parent;
	private String queryURL = "http://"+HOST+":"+PORT+"/time";

	public asyncAddTime(int uid, Context prevCont, HwrkFragment parent, HwrkTime time, Class linked) {
		this.success = false;
		this.errorText = "";
		this.parent = parent;
		this.addClass = linked;
		this.addTime = time;
		// Previous caller's context
		this.prevCont = prevCont;
	}

	// Get the user information in the background and make sure
	@Override
	protected Void doInBackground(String... params) {
		URL url;
		try {
			// Prepare our create method
			url = new URL(queryURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setChunkedStreamingMode(0);

			// Let the server know we are using json
			con.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			// Output the json
			String requestBuffer = "{ \"startTime\": \""+this.addTime.getStartTimeString()+"\", "+
					 "\"endTime\": \""+this.addTime.getEndTimeString()+"\", "+
					"\"theOwner\": \"http://"+HOST+":"+PORT+"/users/"+User.getInstance().getID()+"\", "+
					 "\"theClass\": \"http://"+HOST+":"+PORT+"/classes/"+this.addClass.getClassID()+"\"}";
			out.write(requestBuffer);
			Log.d("Passing?", requestBuffer);
			out.flush();
			Log.d("Passing?", con.getResponseMessage());
			int RCode = con.getResponseCode();
			if(RCode == 201){
				Log.d("Passing?", "Success");
				this.success = true;
			}
		} catch (MalformedURLException e) {
			// Will happen very rarely unless the user is trying something
			// malicious or is using
			// characters that the url cannot comprehend
			errorText = "Something went wrong with the request";
		} catch (IOException e) {
			// Something went wrong with getting the input stream, probably a
			// network error
			errorText = "Connection could not be established, check your network connection";
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if(this.success){
			parent.fetchTimes(this.prevCont);
		}
		return;
	}
}
