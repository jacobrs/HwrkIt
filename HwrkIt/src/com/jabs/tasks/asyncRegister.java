package com.jabs.tasks;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.jabs.globals.BCrypt;
import com.jabs.hwrkit.LoginBackFragment;
import android.os.AsyncTask;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 11th, 2014
 * 
 * @purpose:
 * 
 * 		This class is used to create a new user, it does not
 * do any error checking because it assumes it is being used
 * by another function which does it's own error checking
 * 
 ****************************************************************/
public class asyncRegister extends AsyncTask<String, Void, Void> {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String errorText;
	private Boolean success;
	
	// Prepare everything that we need for creating a user
	public asyncRegister(String email, String password, String firstName, String lastName){
		// Assume failure until we know we created the user
		this.success = false;
		this.errorText = "";
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		
		// Apply bcrypt encryption to the password
		// the user supplied
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
		this.password = hashed;
	}

	// Create the user in the background thread
	@Override
	protected Void doInBackground(String... params) {
		URL url;
		try {
			// Prepare our create method
			url = new URL("http://linux2-cs.johnabbott.qc.ca:30000/users");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setChunkedStreamingMode(0);
			
			// Let the server know we are using json
			con.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			// Output the json
			out.write("{ \"email\": \""+email+"\", \"firstName\": \""+firstName+"\", \"lastName\": " +
					"\""+lastName+"\", \"userType\": \"S\", \"password\": \""+password+"\" }");
			out.flush();
			int RCode = con.getResponseCode();
			if(RCode == 201){
				// Successfully created the user
				success = true;
			}else if(RCode == 409){
				// A conflict occured (Email already in use)
				errorText = "Email is already registered";
			}else{
				// Something else went wrong
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
		LoginBackFragment.setBtnText("Register");
		if(success){
			// Let the user know everything went well
			LoginBackFragment.displayErrors("Successfully created account", false);
		}else{
			// warn the user that the login does not exist
			LoginBackFragment.displayErrors(errorText, true);
		}
		return;
	}
}
