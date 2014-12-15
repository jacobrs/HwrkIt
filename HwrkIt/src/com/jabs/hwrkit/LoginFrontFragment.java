package com.jabs.hwrkit;

import com.jabs.globals.User;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 11th, 2014
 * 
 * @purpose:
 * 
 * 		This class simply displays the front of our login
 * fragment, it also handles flipping to the back of our
 * fragment. It has static functions which allow extenal
 * classes to display errors on this view.
 * 
 ****************************************************************/
public class LoginFrontFragment extends Fragment {
	// STATIC VARIABLES FOR STATIC FUNCTIONS
	static LinearLayout errorLayout;
	static TextView errorTxt;
	static Button loginBtn;
	static Float errPaddingPx;
	static Float initialY;
	static int errorHeight;
	static boolean pauseListener;
	
	// NORMAL GLOBAL VARIABLES
	LoginActivity prevAct;
	Resources r;
	TextView signUpTxt;
	Context prevCont;
	String passwordErr;
	String emailErr;
	String error;
	
	// This is literally needed so you can rotate the screen
	public LoginFrontFragment(){
	}
	
	public LoginFrontFragment(LoginActivity cont){
		// This is used to flip the card
		prevCont = cont.getApplicationContext();
		prevAct = cont;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			Boolean lostInfo = savedInstanceState.getBoolean("LostInfo");
			if(lostInfo){
	        	prevCont = this.getActivity().getApplicationContext();
	        	prevAct = (LoginActivity) this.getActivity();
	        }
		}
		View ret = inflater.inflate(R.layout.activity_login, container, false);
        
        // Get resources for unit conversion
 		r = getResources();
 		
 		// DECLARING STATIC VARIABLES, THESE ARE ALL USED IN A STATIC FUNCTION
 		// Get padding of our error layout
 	 	// change this if our layout padding is change
 		errPaddingPx = 2*TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, r.getDisplayMetrics());
 		// Get the error container
 		errorTxt = (TextView) ret.findViewById(R.id.login_errors);
 		loginBtn = (Button) ret.findViewById(R.id.username_sign_in_button);
 		errorLayout = (LinearLayout) ret.findViewById(R.id.login_errors_layout);
 		
 		// DECLARING NORMAL VARIABLES
 		final Context thisContext = prevCont;
 		final EditText emailTxt = (EditText) ret.findViewById(R.id.email);
 		final EditText passwordTxt = (EditText) ret.findViewById(R.id.password);
 		signUpTxt = (TextView) ret.findViewById(R.id.tvSignUp);
 		passwordErr = "";
 		emailErr = "";
 		pauseListener = false;
 		// This stores all of the errors combined
 		error = "";
 		
 		loginBtn.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				if(!pauseListener){
	 				// Set the starting position of the button once
	 				if(initialY == null){
	 					initialY = loginBtn.getY();
	 				}
	 				// Get all possible errors
	 				emailErr = getGenErrors("Email", emailTxt);
	 				passwordErr = getGenErrors("Password", passwordTxt);
	 				
	 				// If we have errors
	 				if(emailErr != "" || passwordErr != ""){
	 					error = "";
	 					if(emailErr != "")
	 						error += emailErr;
	 					if(passwordErr != "")
	 						error += passwordErr;
	 					// Get rid of the last endline
	 					error = error.substring(0, error.length() - 1);
	 					
	 					// Display the errors
	 					displayErrors(error);
	 				}else{
	 					// Check login info and create the static user
	 					pauseListener = true;
	 					loginBtn.setText("Loading...");
	 					User.Init(thisContext, editToText(emailTxt), editToText(passwordTxt));
	 				}
	 			}
 			}
 		});
 		
 		// Get click event for flipping cards
 		signUpTxt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!pauseListener){
					prevAct.flipCard(false);
				}
			}
 		});
 		
 		return ret;
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("LostInfo", true);
		super.onSaveInstanceState(outState);
	}
	
	// This returns general errors, things like empty variables
	public String getGenErrors(String prepend, EditText field){
		String error = "";
		String eText = editToText(field);
		
		if(eText.length() < 1){
			error = prepend+" cannot be empty\n";
		}
		
		return error;
	}
	
	public String editToText(EditText theEdit){
    	return theEdit.getText().toString();
    }
	
	// This function causes an animation that reveals
	// our error layout and displays the error text passed
	// into it. It can handle 2 errors at a time (the height
	// of the layout)
	public static Void displayErrors(final String errors){
		// Call inner function of this class
		// Get the amount of lines
		// and the size of our font
		float fontSize = errorTxt.getTextSize();
		
		// Calculate the amount we need to translate (animation)
		errorHeight = (int)fontSize;
		errorHeight += errPaddingPx.intValue();
		
		// Where we want the button to slide to
		final float target = initialY+errorHeight;
		
		// Create both animations
		final ObjectAnimator translateY = ObjectAnimator.ofFloat(loginBtn, "y", loginBtn.getY(), target);
		final ObjectAnimator scaleAlpha;
		// Prepare the animation set
		AnimatorSet set = new AnimatorSet();
		final int halfTime;
		// Determine whether we need to fade it in or not
		// NOTE: If the layout is revealed then it will fade out then
		// 		 back in.
		if(errorLayout.getAlpha() > 0.5){
			scaleAlpha = ObjectAnimator.ofFloat(errorLayout, "alpha", 1.0f, 0.001f, 1.0f);
			scaleAlpha.setDuration(1000);
			halfTime = 500;
		}else{
			scaleAlpha = ObjectAnimator.ofFloat(errorLayout, "alpha", 0.001f, 1.0f);
			scaleAlpha.setDuration(500);
			halfTime = 250;
		}
		translateY.setDuration(500);
		// Prepare the animation's update listener
		scaleAlpha.addUpdateListener(new AnimatorUpdateListener(){
			boolean once = true;
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if(animation.getCurrentPlayTime() > halfTime && once){
					once = false;
					errorTxt.setText(errors);
				}
			}
		});
		// Play the set together
		set.playTogether(translateY, scaleAlpha); 
		set.start();
		return null;
	}
	
	public static void setBtnText(String text){
		loginBtn.setText(text);
		pauseListener = false;
	}
	
	public static Fragment newInstance(LoginActivity cont) {
		LoginFrontFragment fragment = new LoginFrontFragment(cont);	
        return fragment;
    }
}