package com.siteshot.siteshot.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Jon on 10/23/2014.
 */
public class SiteShotClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private UserPhoto mUserPhoto;

    public SiteShotClusterItem(double lat, double lng, UserPhoto photo) {
        mPosition = new LatLng(lat, lng);
        mUserPhoto = photo;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public UserPhoto getUserPhoto() {
        return mUserPhoto;
    }

    public void setUserPhoto(UserPhoto photo) {
        mUserPhoto = photo;
    }
}