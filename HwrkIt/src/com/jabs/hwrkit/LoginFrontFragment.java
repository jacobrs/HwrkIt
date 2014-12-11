package com.jabs.hwrkit;

import com.jabs.globals.User;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginFrontFragment extends Fragment {
	// STATIC VARIABLES FOR STATIC FUNCTIONS
	static Button loginBtn;
	static LinearLayout errorLayout;
	static TextView errorTxt;
	static int errorHeight;
	static float errPaddingPx;
	static Float initialY;
	
	// NORMAL GLOBAL VARIABLES
	String emailErr;
	String passwordErr;
	Resources r;
	Context prevCont;
	LoginActivity prevAct;
	String oldError;
	String error;
	
	public LoginFrontFragment(LoginActivity cont){
		prevCont = cont.getApplicationContext();
		prevAct = cont;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
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
 		final TextView signUpTxt = (TextView) ret.findViewById(R.id.tvSignUp);
 		emailErr = "";
 		passwordErr = "";
 		oldError = "";
 		error = "";
 		
 		loginBtn.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				if(initialY == null){
 					initialY = loginBtn.getY();
 				}
 				// Get all possible errors
 				emailErr = getGenErrors("Username", emailTxt);
 				passwordErr = getGenErrors("Password", passwordTxt);
 				
 				// If we have errors
 				if(emailErr != "" || passwordErr != ""){
 					oldError = error;
 					error = "";
 					if(emailErr != "")
 						error += emailErr;
 					if(passwordErr != "")
 						error += passwordErr;
 					// Get rid of the last endline
 					error = error.substring(0, error.length() - 1);
 					
 					displayErrors(error);
 				}else{
 					// Check login info and create the static user
 					User.Init(thisContext, editToText(emailTxt), editToText(passwordTxt));
 				}
 			}
 		});
 		
 		signUpTxt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				prevAct.flipCard();
			}
 		});
 		
 		return ret;
    }
	
	
	public String getGenErrors(String prepend, EditText field){
		String error = "";
		String eText = editToText(field);
		
		if(eText.length() < 1){
			error = prepend+" cannot be empty\n";
		}
		
		return error;
	}
	
	public static Fragment newInstance(LoginActivity cont) {
		LoginFrontFragment fragment = new LoginFrontFragment(cont);	
        return fragment;
    }
	
	public static Void displayErrors(final String errors){
		// Call inner function of this class
		// Get the amount of lines
		// and the size of our font
		float fontSize = errorTxt.getTextSize();
		
		// Calculate the amount we need to translate (animation)
		errorHeight = (int)fontSize;
		errorHeight += (int)errPaddingPx;
		
		final float target = initialY+errorHeight;
		
		final ObjectAnimator translateY = ObjectAnimator.ofFloat(loginBtn, "y", loginBtn.getY(), target);
		final ObjectAnimator scaleAlpha;
		AnimatorSet set = new AnimatorSet();
		final int halfTime;
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
		set.playSequentially(translateY, scaleAlpha); 
		set.start();
		return null;
	}
	
	public String editToText(EditText theEdit){
    	return theEdit.getText().toString();
    }
}