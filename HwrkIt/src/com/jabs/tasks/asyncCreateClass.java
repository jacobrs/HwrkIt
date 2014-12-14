package com.jabs.tasks;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class asyncCreateClass extends AsyncTask<String, Void, Void>{

	private Boolean success;
	private String className;
	private String theTeacher;
	private String errorText;
	
	public asyncCreateClass(String className){
		this.success = false;
		this.className = className;
		// Ian is defualt teacher
		this.theTeacher = "http://linux2-cs.johnabbott.qc.ca:30000/users/11";
		this.errorText = "";
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL url;
		try {
			// Prepare our create method
			url = new URL("http://linux2-cs.johnabbott.qc.ca:30000/classes");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setChunkedStreamingMode(0);
			
			// Let the server know we are using json
			con.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			// Output the json
			out.write("{ \"className\": \""+className+"\", \"theTeacher\": \""+theTeacher+"\" }");
			out.flush();
			int RCode = con.getResponseCode();
			if(RCode == 201){
				// Successfully created the Class
				URL urlTwo;
				try{
					urlTwo = new URL("http://linux2-cs.johnabbott.qc.ca:30000/users");
					HttpURLConnection conTwo = (HttpURLConnection) url.openConnection();
					conTwo.setRequestMethod("POST");
					
					conTwo.setDoInput(true);
					conTwo.setChunkedStreamingMode(0);
					
					// Let the server know we are using json
					conTwo.setRequestProperty("Content-Type", "application/json");
					int RCodeTwo = con.getResponseCode();
					if(RCodeTwo == 201){
						//parse the json received
						//create a scanner
					}
				}catch (MalformedURLException e){
					// Will happen very rarely unless the user is trying something malicious or is using
					// characters that the url cannot comprehend
					errorText = "Something went wrong with the request";
				}catch (IOException e){
					// Something went wrong with getting the input stream, probably a network error
					errorText = "Connection could not be established, check your network connection";
				}			
			}else if(RCode == 409){
				// A conflict occured (Class already exists)
				errorText = "Class is already registered";
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

}
