package com.proxima.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseUser;
import com.proxima.FilterDialog;
import com.proxima.R;
import com.proxima.fragments.CameraFragment;
import com.proxima.fragments.ProximaMapFragment;
import com.proxima.utils.Tracker;

import java.util.Locale;

//
// Created by Andrew Clissold, Rachel Glomski, Jon Wong on 9/11/14.
// Main Activity for the app, creates two fragments for each of the two tabs, one for camera,
// one for map.
//
// Recent Version: 11/26/14
public class TabActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static Context c;

    private final String TAG = TabActivity.class.getName();
    private Boolean didPerformInitialLogin = false;

    private Location lastLocation;
    private Location currentLocation;

    private LocationRequest locationRequest;
    private LocationClient locationClient;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    /*
    Extension of FragmentStatePagerAdapter which intelligently caches
    all active fragments and manages the fragment lifecycles.
    Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
    */
    public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        // Sparse array to keep track of registered fragments in memory
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // method to return user's current location
    public Location getCurrentLocation() {
        ProximaMapFragment test = (ProximaMapFragment) mSectionsPagerAdapter.getRegisteredFragment(0);
        currentLocation = test.getCurrentLocation();
        return currentLocation;
    }

    // method to force redraw of map markers
    public void refreshMark() {
        ProximaMapFragment test = (ProximaMapFragment) mSectionsPagerAdapter.getRegisteredFragment(0);
        test.reDoMarkers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker.getInstance().trackAppOpen(getIntent());

        setContentView(R.layout.activity_tab);

        c = this;

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        mViewPager.setCurrentItem(0);
    }

    // on resume if a user has logged in allow them to stay logged in
    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser() == null) {
            if (didPerformInitialLogin) {
                Log.d(TAG, "user must be logged in");
                finish();
            } else {
                didPerformInitialLogin = true;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Log.d(TAG, "user is logged in");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                // Log user out of current account and create a new login activity
                Tracker.getInstance().trackLogout(ParseUser.getCurrentUser().getUsername());
                ParseUser.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_filter:
                ProximaMapFragment map =
                        (ProximaMapFragment) mSectionsPagerAdapter.getRegisteredFragment(0);
                int selectedIndex = map.getSelectedFilterIndex();
                FilterDialog dialog = new FilterDialog(selectedIndex);
                dialog.setTargetFragment(map, R.integer.FILTER_REQUEST);
                dialog.show(getFragmentManager(), null);
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    // Map fragment
                    return new ProximaMapFragment().newInstance(0);
                case 1:
                    // Camera fragment
                    return new CameraFragment().newInstance(1);
            }

            return null;
        }
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Cam";
            }
            return null;
        }
    }
}
