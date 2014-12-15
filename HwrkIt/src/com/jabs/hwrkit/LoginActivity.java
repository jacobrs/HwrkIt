package com.jabs.hwrkit;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/****************************************************************
 * @author Benjamin Barault
 * @last_modification_date: December 11th, 2014
 * 
 * @purpose:
 * 
 * 		This class handles both the front and back fragment
 * for logging in and registering.
 * 
 ****************************************************************/
public class LoginActivity extends ActionBarActivity {
	private boolean mShowingBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logres_frags);
        
        // Show our custom action bar
        ActionBar actionBar = getSupportActionBar();
 		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
 		actionBar.setCustomView(R.layout.abs_login_layout);
 		
 		// If this is the first time then set the defaults
        if (savedInstanceState == null) {
           mShowingBack = false;
           getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, LoginFrontFragment.newInstance(this))
                    .commit();
        }
	}
	
	// Create the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.login_activity, menu);
		return true;
	}

	// Determine which menu item was selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
	
	// Flip the card and change the fragment view
	public void flipCard(Boolean frontIsLost) {
		// If we're displaying the back then simply
		// remove the back from our stack (animation
		// will occur automatically)
	    if (mShowingBack && !frontIsLost) {
	    	mShowingBack = false;
	        getFragmentManager().popBackStack();
	        return;
	    }else if(frontIsLost){
	    	mShowingBack = false;
	    	// Create and commit a new fragment transaction that adds the fragment for the back of
		    // the card, uses custom animations, and is part of the fragment manager's back stack.
	
		    getFragmentManager()
		            .beginTransaction()
	
		            // Replace the default fragment animations with animator resources representing
		            // rotations when switching to the back of the card, as well as animator
		            // resources representing rotations when flipping back to the front (e.g. when
		            // the system Back button is pressed).
		            .setCustomAnimations(
		                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
		                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
	
		            // Replace any fragments currently in the container view with a fragment
		            // representing the next page (indicated by the just-incremented currentPage
		            // variable).
		            .replace(R.id.container, LoginFrontFragment.newInstance(this))
	
		            // Add this transaction to the back stack, allowing users to press Back
		            // to get to the front of the card.
		            .addToBackStack(null)
	
		            // Commit the transaction.
		            .commit();
	    }else{
		    // Flip to the back.
		    mShowingBack = true;
	
		    // Create and commit a new fragment transaction that adds the fragment for the back of
		    // the card, uses custom animations, and is part of the fragment manager's back stack.
	
		    getFragmentManager()
		            .beginTransaction()
	
		            // Replace the default fragment animations with animator resources representing
		            // rotations when switching to the back of the card, as well as animator
		            // resources representing rotations when flipping back to the front (e.g. when
		            // the system Back button is pressed).
		            .setCustomAnimations(
		                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
		                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
	
		            // Replace any fragments currently in the container view with a fragment
		            // representing the next page (indicated by the just-incremented currentPage
		            // variable).
		            .replace(R.id.container, LoginBackFragment.newInstance(this))
	
		            // Add this transaction to the back stack, allowing users to press Back
		            // to get to the front of the card.
		            .addToBackStack(null)
	
		            // Commit the transaction.
		            .commit();
		}
	}
}
