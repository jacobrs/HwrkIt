package com.jabs.hwrkit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
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
	String usernameErr;
	String passwordErr;
	int errorHeight;
	Resources r;
	Context prevCont;
	LoginActivity prevAct;
	boolean setOnce;
	float initialY;
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
 		setOnce = true;
 		
 		// Get padding of our error layout
 		// change this if our layout padding is change
 		final float errPaddingPx = 2*TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, r.getDisplayMetrics());
 		final Intent mainActivity = new Intent(prevCont, MainActivity.class);
 		final Context thisContext = prevCont;
 		final Button loginBtn = (Button) ret.findViewById(R.id.username_sign_in_button);
 		final EditText usernameTxt = (EditText) ret.findViewById(R.id.username);
 		final EditText passwordTxt = (EditText) ret.findViewById(R.id.password);
 		final TextView errorTxt = (TextView) ret.findViewById(R.id.login_errors);
 		final LinearLayout errorLayout = (LinearLayout) ret.findViewById(R.id.login_errors_layout);
 		final TextView signUpTxt = (TextView) ret.findViewById(R.id.tvSignUp);
 		usernameErr = "";
 		passwordErr = "";
 		oldError = "";
 		error = "";
 		
 		loginBtn.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				if(setOnce){
					setOnce = false;
					initialY = loginBtn.getY();
				}
 				// Get all possible errors
 				usernameErr = getGenErrors("Username", usernameTxt);
 				passwordErr = getGenErrors("Password", passwordTxt);
 				
 				// If we have errors
 				if(usernameErr != "" || passwordErr != ""){
 					oldError = error;
 					error = "";
 					if(usernameErr != "")
 						error += usernameErr;
 					if(passwordErr != "")
 						error += passwordErr;
 					// Get rid of the last endline
 					error = error.substring(0, error.length() - 1);
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
								errorTxt.setText(error);
							}
						}
 					});
 					set.playSequentially(translateY, scaleAlpha); 
 					set.start();
 				}else{
 					startActivity(mainActivity);
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
		String eText = field.getText().toString();
		
		if(eText.length() < 1){
			error = prepend+" cannot be empty\n";
		}
		
		return error;
	}
	
	public static Fragment newInstance(LoginActivity cont) {
		LoginFrontFragment fragment = new LoginFrontFragment(cont);	
        return fragment;
    }
}