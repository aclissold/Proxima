package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import com.siteshot.siteshot.R;
import com.siteshot.siteshot.adapters.ClusterImageAdapter;
import com.siteshot.siteshot.adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jonathan on 11/2/2014.
 */
public class ClusterViewActivity extends Activity {

    int count;
    String currentUser;
    String[] clusterArr;
    String[] discoveredArr;
    ArrayList<String> clusterContents;
    ArrayList<String> newlyDiscovered;

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
        GridView photoGrid = (GridView) findViewById(R.id.cluster_photo_grid);
        photoGrid.setAdapter(new ClusterImageAdapter(this, this));




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
