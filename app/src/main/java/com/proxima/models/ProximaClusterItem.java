package com.proxima.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

///
// Created by Andrew Clissold, Rachel Glomski, Jon Wong on 10/23/2014.
// Model for a cluster item
//
// Recent Version: 11/26/14
public class ProximaClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private UserPhoto mUserPhoto;

    public ProximaClusterItem(double lat, double lng, UserPhoto photo) {
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
