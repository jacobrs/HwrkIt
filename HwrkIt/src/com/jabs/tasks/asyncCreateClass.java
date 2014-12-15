package com.jabs.tasks;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jabs.globals.BCrypt;
import com.jabs.hwrkit.ClassFragment;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class asyncCreateClass extends AsyncTask<String, Void, Void>{

	private Boolean successGET;
	private Boolean successPOST;
	private Boolean alreadyExists;
	private String className;
	private int userID;
	private int classID;
	private String theTeacher;
	private String errorText;
	
	public asyncCreateClass(String className, int userID){
		this.successPOST = false;
		this.successGET = false;
		this.alreadyExists = false;
		this.className = className;
		this.userID = userID;
		// set default
		this.classID = 0;
		// Ian is defualt teacher
		this.theTeacher = "http://linux2-cs.johnabbott.qc.ca:30000/users/34";
		this.errorText = "";
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		// check if class already exists
		URL urlCheck;
		try{
			urlCheck = new URL("http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/api/getUsersClasses.php?uid="+userID);
			HttpURLConnection conCheck = (HttpURLConnection) urlCheck.openConnection();
			conCheck.setRequestMethod("GET");
			
			conCheck.setDoInput(true);
			conCheck.setChunkedStreamingMode(0);
			
			// Let the server know we are using json
			conCheck.setRequestProperty("Content-Type", "application/json");
			int RCodeCheck = conCheck.getResponseCode();
			if(RCodeCheck == 200){
				Log.d("Got JSON first time", "Success");
				//parse the json received
				//create a scanner
				Scanner scanner = new Scanner(conCheck.getInputStream()).useDelimiter("\\A");
				String json = scanner.hasNext() ? scanner.next() : "";
				
				// Parse the JSON and get all the appropriate information
				JSONTokener tokener = new JSONTokener(json);
				try{
					JSONObject root = (JSONObject) tokener.nextValue();
					if(root.has("classes")){	
						Log.d("Got Embedded first time", "Success");
						JSONArray classes = root.getJSONArray("classes");
						
						// loop over as many classes as you have
						// end loop when previously inputed class is found
						for(int i = 0; i < classes.length(); i++){
							JSONObject currClass = classes.getJSONObject(i);
							String name = currClass.getString("Name");
							Log.d("Current Class Name", name.toString());
							if(name.equals(className)){
								Log.d("Found Class", "Failure");
								alreadyExists = true;
								break;
							}
						}
					}else{
						// no classes have been created
						alreadyExists = false;
					}
				}catch(JSONException e){
					errorText = "Parsing Error";
				}
			}
		}catch (MalformedURLException e){
			// Will happen very rarely unless the user is trying something malicious or is using
			// characters that the url cannot comprehend
			errorText = "Something went wrong with the request";
		}catch (IOException e){
			// Something went wrong with getting the input stream, probably a network error
			errorText = "Connection could not be established, check your network connection";
		}
		
		if(!alreadyExists){
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
					Log.d("Created Class", "Success");
					// Successfully created the Class
					// Get the class id of the class just posted
					
					URL urlTwo;
					String href = "";
					try{
						urlTwo = new URL("http://linux2-cs.johnabbott.qc.ca:30000/classes");
						HttpURLConnection conTwo = (HttpURLConnection) urlTwo.openConnection();
						conTwo.setRequestMethod("GET");
						
						conTwo.setDoInput(true);
						conTwo.setChunkedStreamingMode(0);
						
						// Let the server know we are using json
						conTwo.setRequestProperty("Content-Type", "application/json");
						int RCodeTwo = conTwo.getResponseCode();
						if(RCodeTwo == 200){
							Log.d("Got JSON", "Success");
							//parse the json received
							//create a scanner
							Scanner scanner = new Scanner(conTwo.getInputStream()).useDelimiter("\\A");
							String json = scanner.hasNext() ? scanner.next() : "";
							
							// Parse the JSON and get all the appropriate information
							JSONTokener tokener = new JSONTokener(json);
							try{
								JSONObject root = (JSONObject) tokener.nextValue();
								if(root.has("_embedded")){	
									Log.d("Got Embedded", "Success");
									JSONObject realRoot = root.getJSONObject("_embedded");
									JSONArray res = realRoot.getJSONArray("classes");
									
									// loop over as many classes as you have
									// end loop when previously inputed class is found
									for(int i = 0; i < res.length(); i++){
										JSONObject currClass = res.getJSONObject(i);
										String name = currClass.getString("className");
										Log.d("Current Class Name", name.toString());
										if(name.equals(className)){
											Log.d("Found Class", "Success");
											// inputed class found
											// Get the user's classes
											JSONObject linksRoot = currClass.getJSONObject("_links");
											JSONObject linksSelf = linksRoot.getJSONObject("self");
											
											// Link to all of this user's classes
											href = linksSelf.getString("href");
											successGET = true;
										}
									}
									if(successGET){
										// got the class ID of the class just posted
										String[] splitHref = href.split("/");
										href = splitHref[splitHref.length-1];
										classID = Integer.parseInt(href);
										
										// post to assignations
										Log.d("Post to Assignations", "Success");
										URL urlThree;
										try {
											// Prepare our create method
											urlThree = new URL("http://linux2-cs.johnabbott.qc.ca:30000/assignations");
											HttpURLConnection conThree = (HttpURLConnection) urlThree.openConnection();
											conThree.setRequestMethod("POST");
											
											conThree.setDoInput(true);
											conThree.setDoOutput(true);
											conThree.setChunkedStreamingMode(0);
											
											// Let the server know we are using json
											conThree.setRequestProperty("Content-Type", "application/json");
											OutputStreamWriter outTwo = new OutputStreamWriter(conThree.getOutputStream());
											// Output the json
											outTwo.write("{ \"theUser\": \"http://linux2-cs.johnabbott.qc.ca:30000/users/"+userID+"\", \"theClass\": \"http://linux2-cs.johnabbott.qc.ca:30000/classes/"+classID+"\" }");
											outTwo.flush();
											int RCodeThree = conThree.getResponseCode();
											if(RCodeThree == 201){
												// Successfully posted to assignations
												Log.d("Successful Post", "Success");
												successPOST = true;
											}
										}catch(MalformedURLException e){
											// Will happen very rarely unless the user is trying something malicious or is using
											// characters that the url cannot comprehend
											errorText = "Something went wrong with the request";
										}catch (IOException e){
											// Something went wrong with getting the input stream, probably a network error
											errorText = "Connection could not be established, check your network connection";
										}			
									}
								}else{
									// Class probably does not exist
									// Shouldn't ever happen
									errorText = "Class doesn't exist";
								}
							}catch (JSONException e){
								// shouldn't really happen
								errorText = "Parsing failed";
							}
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
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void v) {
		if(alreadyExists){
			ClassFragment.ClassExists();
			Log.d("EXISTS", "SHOULD ALREADY EXIST");
		}else{
			ClassFragment.AddToList(className, classID);
		}
		return;
	}

}
