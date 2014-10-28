package com.siteshot.siteshot.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Jon on 10/23/2014.
 */
public class SiteShotCluster implements ClusterItem {
    private final LatLng mPosition;

    public SiteShotCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}