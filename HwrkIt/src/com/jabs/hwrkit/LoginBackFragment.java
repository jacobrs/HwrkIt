package com.jabs.hwrkit;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Toast;

public class LoginBackFragment extends Fragment {
	LoginActivity prevAct;
	String usernameErr;
	String passwordErr;
	int errorHeight;
	Resources r;
	Context prevCont;
	boolean error;
	int counter;
	
	public LoginBackFragment(LoginActivity cont){
		prevCont = cont.getApplicationContext();
		prevAct = cont;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.activity_register, container, false);

        error = false;
        
        r = getResources();
 		
 		// Get padding of our error layout
 		// change this if our layout padding is changed
 		final float errPaddingPx = 2*TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, r.getDisplayMetrics());
 		final Context thisContext = prevCont;
 		final Animation errorAnimation = AnimationUtils.loadAnimation(thisContext, R.animator.login_errors_animation);
 		final Animation fixAnimation = AnimationUtils.loadAnimation(thisContext, R.animator.login_fix_animation);
 		
 		
 		final LinearLayout errorLayout = (LinearLayout) ret.findViewById(R.id.register_errors_layout);
        final TextView errorTxt = (TextView) ret.findViewById(R.id.register_errors);
 		final TextView backTxt = (TextView) ret.findViewById(R.id.tvBack);
        final Button registerBtn = (Button) ret.findViewById(R.id.register_button);
        final EditText usernameTxt = (EditText) ret.findViewById(R.id.rUsername);
        final EditText passwordTxt = (EditText) ret.findViewById(R.id.rPassword);
        final EditText repasswordTxt = (EditText) ret.findViewById(R.id.rsPassword);
        usernameErr = "";
 		passwordErr = "";
        counter = 0;
 		
        registerBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// Get all possible errors
				Log.d("Okay", ""+counter);
				counter++;
 				usernameErr = getGenErrors("Username", usernameTxt);
 				passwordErr = getGenErrors("Password", passwordTxt);
 				
 				if(passwordErr == ""){
 					if(!passwordTxt.getText().toString().equals(repasswordTxt.getText().toString())){
 						passwordErr += "Passwords do not match\n";
 					}
 				}
 				
 				// If we have errors
 				if(usernameErr != "" || passwordErr != ""){
 					error = true;
 					String error = "";
 					if(usernameErr != "")
 						error += usernameErr;
 					if(passwordErr != "")
 						error += passwordErr;
 					// Get rid of the last endline
 					error = error.substring(0, error.length() - 1);
 					errorTxt.setText(error);
 					
 					// Get the amount of lines
 					// and the size of our font
 					int count = error.length() - error.replace("\n", "").length();
 					float fontSize = errorTxt.getTextSize();
 					count++;
 					
 					// Calculate the amount we need to translate (animation)
 					errorHeight = (int)fontSize*count;
 					errorHeight += (int)errPaddingPx;
 					
 					// Change the duration based off of login_error_animation.xml's duration
 					// Change the -y to the height of the error container
 					TranslateAnimation anim=new TranslateAnimation(0,0,-errorHeight,0);
 			        anim.setFillAfter(true);
 			        // Change this if anim/login_errors_animation's duration changes
 			        anim.setDuration(500);
 			        // Animate the button downwards
 					registerBtn.setEnabled(true);
 		            registerBtn.startAnimation(anim);
 		            // Slide down the error layout (Change it from gone->invisible first)
 		            errorLayout.setVisibility(1);
 					errorLayout.startAnimation(errorAnimation);
 				}else{
 					if(error){
 						error = false;
	 					TranslateAnimation anim=new TranslateAnimation(0,0,0,-errorHeight);
	 			        anim.setFillAfter(true);
	 			        // Change this if anim/login_errors_animation's duration changes
	 			        anim.setDuration(500);
	 			        // Animate the button upwards
	 					registerBtn.setEnabled(true);
	 		            registerBtn.startAnimation(anim);
	 		            // Slide up the error layout
	 					errorLayout.startAnimation(fixAnimation);
 					}
 					
 					// Toast here
 					CharSequence text = "Register Stub";
 					int duration = Toast.LENGTH_SHORT;
 					
 					Toast toast = Toast.makeText(prevCont, text, duration);
 					toast.show();
 				}
			}
        });
        
        backTxt.setOnClickListener(new OnClickListener(){
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
		LoginBackFragment fragment = new LoginBackFragment(cont);	
        return fragment;
    }
}