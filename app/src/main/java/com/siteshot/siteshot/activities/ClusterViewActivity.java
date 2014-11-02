package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

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
    String[] clusterArr;
    ArrayList<String> clusterContents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        clusterArr = extras.getStringArray("cluster");
        clusterContents = new ArrayList<String>(Arrays.asList(clusterArr));
        count = clusterContents.size();
        setContentView(R.layout.activity_cluster_view);
        GridView photoGrid = (GridView) findViewById(R.id.cluster_photo_grid);
        photoGrid.setAdapter(new ClusterImageAdapter(this, this));

    }

    public int getCount(){
        return count;
    }

    public String[] pushArray(){
        return clusterArr;
    }
}
