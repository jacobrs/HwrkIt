package com.jabs.hwrkit;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LoginBackFragment extends Fragment {
	LoginActivity prevAct;
	
	public LoginBackFragment(LoginActivity cont){
		prevAct = cont;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.activity_register, container, false);
        
        TextView backTxt = (TextView) ret.findViewById(R.id.tvBack);
        
        
        backTxt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				prevAct.flipCard();
			}
        });
        
        return ret;
    }
    
    public static Fragment newInstance(LoginActivity cont) {
		LoginBackFragment fragment = new LoginBackFragment(cont);	
        return fragment;
    }
}