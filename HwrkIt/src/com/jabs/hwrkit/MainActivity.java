package com.jabs.hwrkit;

import java.util.ArrayList;
import java.util.List;

import com.jabs.adapters.ClassesAdapter;
import com.jabs.globals.User;
import com.jabs.structures.Class;
import com.jabs.structures.HwrkTime;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        /*    TESTING SINGLTON, KEEP THIS FOR LATER ON WHEN WE HAVE
         * 	  TO DO MORE TESTING
        User theUser = User.getInstance();
  		ArrayList<Class> hisClasses = theUser.getClasses();
   		for(int i = 0; i < hisClasses.size(); i++){
   			ArrayList<HwrkTime> classesTimes = hisClasses.get(i).getTimes();
   			for(int j = 0; j < classesTimes.size(); j++){
   				HwrkTime theTime = classesTimes.get(j);
   				Log.d("START", theTime.getStartTime().toString());
   				Log.d("END", theTime.getEndTime().toString());
   			}
   		}
        */
        
        
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    	Fragment fragment = new Fragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
	    switch(position) {
	        case 0:
                mTitle = getString(R.string.title_section1);
                restoreActionBar();
	        	fragment = new StatisticsFragment();
	            break;
	        case 1:
                mTitle = getString(R.string.title_section2);
                restoreActionBar();
	        	fragment = new ClassFragment();
	            break;
	        case 2:
                mTitle = getString(R.string.title_section3);
                restoreActionBar();
	        	fragment = new HwrkFragment();
	            break;
	        default : 
	            break;
	    }
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						fragment).commit();
		
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	User.deleteInstance();
        	Intent loginActivity = new Intent(this.getApplicationContext(), LoginActivity.class);
        	this.startActivity(loginActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	//getFragmentManager().beginTransaction().replace(R.id.container, this).addToBackStack(null).commit();
        	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
