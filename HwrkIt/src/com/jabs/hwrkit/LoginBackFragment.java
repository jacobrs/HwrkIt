package com.jabs.hwrkit;

import com.jabs.globals.User;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.Toast;

public class LoginBackFragment extends Fragment {
	// STATIC VARIABLES FOR STATIC FUNCTIONS
	static TextView errorTxt;
	static LinearLayout errorLayout;
	static Button registerBtn;
	static int errorHeight;
	static float errPaddingPx;
	static float initialY;
	
	// NORMAL VARIABLES FOR NORMAL FUNCTIONS
	Resources r;
	LoginActivity prevAct;
	Context prevCont;
	String Errors;
	String oldError;
	String error;
	boolean setOnce;
	
	public LoginBackFragment(LoginActivity cont){
		prevCont = cont.getApplicationContext();
		prevAct = cont;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.activity_register, container, false);

        // Makes sure something is set once (because it moves)
        setOnce = true;
 		
        r = getResources();
        
        // SETTING STATIC VARIABLES
        errorTxt = (TextView) ret.findViewById(R.id.register_errors);
        // Padding of out error layout
  		errPaddingPx = 2*TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, r.getDisplayMetrics());	
  		errorLayout = (LinearLayout) ret.findViewById(R.id.register_errors_layout);
  		registerBtn = (Button) ret.findViewById(R.id.register_button);
  		
        // SETTING NORMAL VARIABLES
 		final TextView backTxt = (TextView) ret.findViewById(R.id.tvBack);        
        final EditText fnameTxt = (EditText) ret.findViewById(R.id.rFName);
        final EditText lnameTxt = (EditText) ret.findViewById(R.id.rLName);
        final EditText emailTxt = (EditText) ret.findViewById(R.id.rEmail);
        final EditText passwordTxt = (EditText) ret.findViewById(R.id.rPassword);
        final EditText repasswordTxt = (EditText) ret.findViewById(R.id.rsPassword);
 		error = "";
 		oldError = "";
 		
        registerBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(setOnce){
					setOnce = false;
					initialY = registerBtn.getY();
				}
				// Get all possible errors
 				Errors = getGenErrors(editToText(emailTxt), editToText(passwordTxt), 
 						editToText(fnameTxt), editToText(lnameTxt));
 				
 				Log.d("ERRORS", Errors);
 				
 				if(Errors == ""){
 					if(!passwordTxt.getText().toString().equals(repasswordTxt.getText().toString())){
 						Errors += "Passwords do not match";
 					}
 				}
 				
 				// If we have errors
 				if(Errors != ""){
 					oldError = error;
 					error = Errors;
 					displayErrors(error, true);
 				}else{
	 				// Register the user
 					User.registerUser(editToText(emailTxt), editToText(passwordTxt), editToText(fnameTxt), editToText(lnameTxt));
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
    
    // This function simply gets the text from an edittext
    // and returns it's string value
    public String editToText(EditText theEdit){
    	return theEdit.getText().toString();
    }
    
    // This function simply generates general errors
    // Things like empty texts and whatnot
    public String getGenErrors(String email, String password, String firstName, String lastName){
		String error = "";
		int numErrors = 0;
		
		if(email.length() < 1){
			error += "email, ";
			numErrors++;
		}
		if(password.length() < 1){
			error += "password, ";
			numErrors++;
		}
		if(firstName.length() < 1){
			error += "first name, ";
			numErrors++;
		}
		if(lastName.length() < 1){
			error += "last name, ";
			numErrors++;
		}
		
		if(error.length() > 2){
			error = error.substring(0, error.length() - 2);
		}
		
		if(numErrors > 0){
			if(numErrors > 1){
				// OLC = Of Last Comma
				int indexOLC = error.lastIndexOf(",");
				error = error.substring(0, indexOLC) + " and" + error.substring(indexOLC + 1);
			}
			error += " cannot be empty";
			error = Character.toUpperCase(error.charAt(0)) + error.substring(1);
		}
		
		return error;
	}
    
    // Creates a new instance of the back card
    public static Fragment newInstance(LoginActivity cont) {
		LoginBackFragment fragment = new LoginBackFragment(cont);	
        return fragment;
    }
    
    // isError determines the color of our error layout
    public static Void displayErrors(final String errors, final boolean isError){
    	// Get the amount of lines
		// and the size of our font
		float fontSize = errorTxt.getTextSize();
		
		// Calculate the amount we need to translate (animation)
		errorHeight = (int)fontSize;
		errorHeight += (int)errPaddingPx;
		
		final float target = initialY+errorHeight;
		
		Log.d("target", ""+target);
		Log.d("getY", ""+registerBtn.getY());
		
		final ObjectAnimator translateY = ObjectAnimator.ofFloat(registerBtn, "y", registerBtn.getY(), target);
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
					if(isError){
			    		errorLayout.setBackgroundResource(R.drawable.custom_login_error_layout);
			    	}else{
			    		errorLayout.setBackgroundResource(R.drawable.custom_login_success_layout);
			    	}
				}
			}
		});
		set.playSequentially(translateY, scaleAlpha); 
		set.start();
    	return null;
    }
}