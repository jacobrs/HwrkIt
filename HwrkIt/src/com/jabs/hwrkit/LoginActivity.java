package com.jabs.hwrkit;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ActionBar actionBar = getSupportActionBar();
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.abs_login_layout);
		
		Button login = (Button)findViewById(R.id.username_sign_in_button);
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Verify successful login
				
				// inflate new view
				Toast toast = Toast.makeText(getApplicationContext(), "Login was clicked!", Toast.LENGTH_SHORT);
				toast.show();
				// LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// View mainView = inflater.inflate(R.layout.activity_main, null);
				// need to inflate the main page...
			}	
			
		});
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
