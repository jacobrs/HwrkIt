package com.jabs.hwrkit;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
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
	boolean bError;
	boolean setOnce;
	float initialY;
	String oldError;
	String error;
	
	public LoginBackFragment(LoginActivity cont){
		prevCont = cont.getApplicationContext();
		prevAct = cont;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.activity_register, container, false);

        bError = false;
        setOnce = true;
        
        r = getResources();
 		
 		// Get padding of our error layout
 		// change this if our layout padding is changed
        final float registerBtnMarg = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
 		final float errPaddingPx = 2*TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, r.getDisplayMetrics());
 		final Context thisContext = prevCont;
 		
 		
 		final LinearLayout errorLayout = (LinearLayout) ret.findViewById(R.id.register_errors_layout);
        final TextView errorTxt = (TextView) ret.findViewById(R.id.register_errors);
 		final TextView backTxt = (TextView) ret.findViewById(R.id.tvBack);
        final Button registerBtn = (Button) ret.findViewById(R.id.register_button);
        final EditText usernameTxt = (EditText) ret.findViewById(R.id.rUsername);
        final EditText passwordTxt = (EditText) ret.findViewById(R.id.rPassword);
        final EditText repasswordTxt = (EditText) ret.findViewById(R.id.rsPassword);
        usernameErr = "";
 		passwordErr = "";
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
 				usernameErr = getGenErrors("Username", usernameTxt);
 				passwordErr = getGenErrors("Password", passwordTxt);
 				
 				if(passwordErr == ""){
 					if(!passwordTxt.getText().toString().equals(repasswordTxt.getText().toString())){
 						passwordErr += "Passwords do not match\n";
 					}
 				}
 				
 				// If we have errors
 				if(usernameErr != "" || passwordErr != ""){
 					bError = true;
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
								errorTxt.setText(error);
							}
						}
 					});
 					set.playSequentially(translateY, scaleAlpha); 
 					set.start();
 				}else{
 					if(bError){
 						bError = false;
 						float target = initialY;
 	 					
 	 					final ObjectAnimator translateY = ObjectAnimator.ofFloat(registerBtn, "y", registerBtn.getY(), target);
 	 					final ObjectAnimator scaleAlpha = ObjectAnimator.ofFloat(errorLayout, "alpha", 1.0f,  0.001f);
 	 					translateY.setDuration(500);
 	 		            scaleAlpha.setDuration(500);
 	 		            scaleAlpha.addListener(new AnimatorListener(){
 							@Override
 							public void onAnimationStart(Animator animation) {
 								// TODO Auto-generated method stub
 							}
 							@Override
 							public void onAnimationEnd(Animator animation) {
 								// TODO Auto-generated method stub
 								translateY.start();
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
 	 		            scaleAlpha.start();
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