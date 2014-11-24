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

/**
 * Created by Jonathan on 11/2/2014.
 */
public class ClusterViewActivity extends ActionBarActivity {

    int count;
    String currentUser;
    String[] clusterArr;
    String[] discoveredArr;
    ArrayList<String> clusterContents;
    ArrayList<String> newlyDiscovered;

    ImageButton mExitButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        currentUser = extras.getString("currentUser");
        clusterArr = extras.getStringArray("cluster");
        discoveredArr = extras.getStringArray("discovered");
        clusterContents = new ArrayList<String>(Arrays.asList(clusterArr));
        newlyDiscovered = new ArrayList<String>(Arrays.asList(discoveredArr));
        count = clusterContents.size();
        setContentView(R.layout.activity_cluster_view);
        mExitButton = (ImageButton) findViewById(R.id.exitButton);
        GridView photoGrid = (GridView) findViewById(R.id.cluster_photo_grid);
        photoGrid.setAdapter(new ClusterImageAdapter(this, this));

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    public int getCount(){
        return count;
    }

    public String getUser(){
        return currentUser;
    }

    public String[] pushArray(){
        return clusterArr;
    }

    public String[] pushDiscovered(){
        return discoveredArr;
    }
}
