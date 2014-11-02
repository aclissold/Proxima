package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.siteshot.siteshot.R;
import com.siteshot.siteshot.adapters.ClusterImageAdapter;
import com.siteshot.siteshot.adapters.ImageAdapter;

import java.util.ArrayList;

/**
 * Created by Jonathan on 11/2/2014.
 */
public class ClusterViewActivity extends Activity {

    int count;
    ArrayList<String> clusterContents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get extras containing photo data and rotate flag
        Bundle extras = getIntent().getExtras();

        clusterContents = extras.getStringArrayList("cluster");
        count = clusterContents.size();

        setContentView(R.layout.activity_cluster_view);
        GridView photoGrid = (GridView) findViewById(R.id.cluster_photo_grid);
        photoGrid.setAdapter(new ClusterImageAdapter(this));

    }

    public int getCount(){
        return count;
    }

    public ArrayList<String> pushArray(){
        return clusterContents;
    }
}
