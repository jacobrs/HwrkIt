package com.jabs.tasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jabs.globals.BCrypt;
import com.jabs.hwrkit.LoginFrontFragment;
import com.jabs.hwrkit.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 12th, 2014
 * 
 * @purpose:
 * 
 * 		This class is used to log a user in, it does error handling
 * on the appropriate activity. It will start the main activity of
 * the application after it is done verifying that everything is okay.
 * If there was an error with the login it will notify the user.
 * 
 ****************************************************************/
public class asyncLogin extends AsyncTask<String, Void, Void> {
	private String email;
	private String password;
	private Boolean success;
	private Context prevCont;
	private String errorText;
	private final Intent mainActivity;
	
	// Prepare everything that we need, we need the context because
	// we are creating with an intent with the purpose of starting
	// a new activity (potentially) later on
	public asyncLogin(String email, String password, Context prevCont) {
		this.success = false; // Assume failure until we know the password matches
		this.errorText = "";
		this.email = email;
		this.prevCont = prevCont;
		this.mainActivity = new Intent(prevCont, MainActivity.class);
		this.mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.password = password;
	}

	// Get the user information in the background and make sure
	// the password matches the user's
	@Override
	protected Void doInBackground(String... params) {
		URL url;
		try {
			url = new URL("http://linux2-cs.johnabbott.qc.ca:30000/users/search/findByEmail?email="+email);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			con.setDoInput(true);
			con.setChunkedStreamingMode(0);
			
			if(con.getResponseCode() == 200){
				Scanner scanner = new Scanner(con.getInputStream()).useDelimiter("\\A");
				String json = scanner.hasNext() ? scanner.next() : "";
				
				JSONTokener tokener = new JSONTokener(json);
				try {	
					JSONObject root = (JSONObject) tokener.nextValue();
					// 
					if(root.has("_embedded")){					
						JSONObject realRoot = root.getJSONObject("_embedded");
						JSONArray res = realRoot.getJSONArray("users");
						
						JSONObject user = res.getJSONObject(0);
						String password = user.getString("password");
						if(!BCrypt.checkpw(this.password, password)){
							errorText = "Email or password was incorrect";
						}else{
							success = true;
						}
					}else{
						errorText = "Email or password was incorrect";
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
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if(success){
			this.prevCont.startActivity(mainActivity);
		}else{
			// warn the user that the login does not exist
			LoginFrontFragment.displayErrors(errorText);
		}
		return;
	}
}