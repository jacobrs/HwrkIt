package com.jabs.hwrkit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
 					int count = error.length() - error.replace("\n", "").length();
 					float fontSize = errorTxt.getTextSize();
 					count++;
 					
 					// Calculate the amount we need to translate (animation)
 					errorHeight = (int)fontSize*count;
 					errorHeight += (int)errPaddingPx;
 					
 					float target = initialY+errorHeight;
 					
 					final ObjectAnimator translateY = ObjectAnimator.ofFloat(loginBtn, "y", loginBtn.getY(), target);
 					final ObjectAnimator scaleAlpha;
 					if(errorLayout.getAlpha() > 0.5){
 						scaleAlpha = ObjectAnimator.ofFloat(errorLayout, "alpha", 1.0f, 0.001f, 1.0f);
 						scaleAlpha.setDuration(1000);
 					}else{
 						scaleAlpha = ObjectAnimator.ofFloat(errorLayout, "alpha", 0.001f, 1.0f);
 						scaleAlpha.setDuration(500);
 					}
 					translateY.setDuration(500);
 		            translateY.addListener(new AnimatorListener(){
						@Override
						public void onAnimationStart(Animator animation) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onAnimationEnd(Animator animation) {
							// TODO Auto-generated method stub
							errorTxt.setText(error);
							scaleAlpha.start();
						}
						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onAnimationRepeat(Animator animation) {
							// TODO Auto-generated method stub
						}
 		            });
 		            if(!oldError.equals(error)){
 		            	translateY.start();
 		            }else{
 		            	scaleAlpha.start();
 		            }
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