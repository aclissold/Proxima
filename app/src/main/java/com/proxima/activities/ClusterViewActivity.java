package com.proxima.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import com.proxima.R;
import com.proxima.adapters.ClusterImageAdapter;

import java.util.ArrayList;
import java.util.Arrays;

//
// Created by Andrew Clissold, Rachel Glomski, and Jonathan Wong on 11/2/2014.
// Activity to view photos in a map cluster, sets a photo grid which is then
// populated by the the photos in the cluster
//
// Recent Version: 11/25/14
public class ClusterViewActivity extends ActionBarActivity {

    // variables to retrieve data bundled with extras
    int count;
    String currentUser;
    String[] clusterArr; // object IDs of photos in the cluster
    String[] discoveredArr; // object IDs of newly discovered photos in the cluster

    // variables to hold retrieved string data as a list
    ArrayList<String> clusterContents;
    ArrayList<String> newlyDiscovered;

    ImageButton mExitButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve data bundled in extras
        Bundle extras = getIntent().getExtras();
        currentUser = extras.getString("currentUser");
        clusterArr = extras.getStringArray("cluster");
        discoveredArr = extras.getStringArray("discovered");

        // convert string data to lists
        clusterContents = new ArrayList<String>(Arrays.asList(clusterArr));
        newlyDiscovered = new ArrayList<String>(Arrays.asList(discoveredArr));

        // get the amount of photos in the cluster
        count = clusterContents.size();

        // set UI
        setContentView(R.layout.activity_cluster_view);
        mExitButton = (ImageButton) findViewById(R.id.exitButton);
        GridView photoGrid = (GridView) findViewById(R.id.cluster_photo_grid);

        // set adapter for the photo grid
        photoGrid.setAdapter(new ClusterImageAdapter(this, this));

        // set click listener for the exit button
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // returns the size of the cluster for the photo grid adapter
    public int getCount(){
        return count;
    }

    // returns the current user
    public String getUser(){
        return currentUser;
    }

    // returns the objectIDs of the photos in a cluster
    public String[] pushArray(){
        return clusterArr;
    }

    // returns the objectIDs of newly discovered photos in a cluster
    public String[] pushDiscovered(){
        return discoveredArr;
    }
}
