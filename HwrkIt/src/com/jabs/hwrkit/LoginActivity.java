package com.jabs.hwrkit;

import java.util.ArrayList;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends ActionBarActivity {

	String usernameErr;
	String passwordErr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBar.setCustomView(R.layout.abs_login_layout);
		
		Button loginBtn = (Button) findViewById(R.id.username_sign_in_button);
		final EditText usernameTxt = (EditText) findViewById(R.id.username);
		final EditText passwordTxt = (EditText) findViewById(R.id.password);
		final TextView errorTxt = (TextView) findViewById(R.id.login_errors);
		usernameErr = "";
		passwordErr = "";
		
		final Intent mainActivity = new Intent(this, MainActivity.class);
		
		loginBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usernameErr = getGenErrors("Username", usernameTxt);
				passwordErr = getGenErrors("Password", passwordTxt);

				//if(usernameErr != "" || passwordErr != ""){
					//errorTxt.setText(usernameErr+"\n"+passwordErr);
				//}else{
					startActivity(mainActivity);
				//}
			}
		});
	}
	
	public String getGenErrors(String prepend, EditText field){
		String error = "";
		String eText = field.getText().toString();
		
		if(eText.length() < 1){
			error = prepend+" cannot be empty";
		}
		
		return error;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.login_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}
