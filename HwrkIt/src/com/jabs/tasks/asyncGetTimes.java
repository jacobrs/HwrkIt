package com.jabs.tasks;

import java.io.IOException;
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

public class asyncGetTimes extends AsyncTask<String, Void, Void> {
	private int userID;
	private String tmpclass;
	private String errorText;
	private Boolean success;
	private Context prevCont;
	private LinkedList<HwrkTime> times = new LinkedList<HwrkTime>();
	private HwrkFragment parent;
	private String queryURL = "http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/api/getUsersClasses.php?uid=";

	// Prepare everything that we need, we need the context because
	// we are creating with an intent with the purpose of starting
	// a new activity (potentially) later on
	public asyncGetTimes(int uid, Context prevCont, HwrkFragment parent) {
		// Assume failure until we know the password matches
		// and we have received all of the class information
		this.success = false;
		this.errorText = "";
		this.parent = parent;
		// Previous caller's context
		this.prevCont = prevCont;
		queryURL += uid;
	}

	// Get the user information in the background and make sure
	@Override
	protected Void doInBackground(String... params) {
		URL url;
		String href = "";
		try {
			// Create the GET request
			url = new URL(queryURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setDoInput(true);
			con.setChunkedStreamingMode(0);

			if (con.getResponseCode() == 200) {
				// Get the user's information
				Scanner scanner = new Scanner(con.getInputStream())
						.useDelimiter("\\A");
				String json = scanner.hasNext() ? scanner.next() : "";

				// Parse the JSON and get all the appropriate information
				JSONTokener tokener = new JSONTokener(json);
				try {
					JSONObject root = (JSONObject) tokener.nextValue();
					//
					if (root.has("classes")) {
						JSONArray classes = root.getJSONArray("classes");

						for (int i = 0; i < classes.length(); i++) {
							JSONObject tmp = classes.getJSONObject(i);
							tmpclass = tmp.getString("Name");
							Class obj = new Class(tmpclass);
							JSONArray tms = tmp.getJSONArray("times");
							for (int k = 0; k < tms.length(); k++) {
								JSONObject hw = tms.getJSONObject(k);
								times.add(new HwrkTime(hw
										.getString("startTime"), hw
										.getString("endTime"), obj));
							}
						}
					} else {
						// User probably does not exist
						errorText = "Incorrect Api Request";
					}
				} catch (JSONException e) {
					// shouldn't really happen
					errorText = "Parsing failed";
				}
			} else {
				errorText = "Connection could not be established, check your network connection";
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
		parent.times = this.times;
		parent.setListView();
		return;
	}
}
