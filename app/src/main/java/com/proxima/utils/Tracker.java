package com.proxima.utils;

import android.content.Intent;

import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

///
// Created by Andrew Clissold, Rachel Glomski, Jon Wong on 11/11/14.
//
// Abstracts ParseAnalytics method calls to one-liners.
//
// Recent Version: 11/26/14
public class Tracker {

    private static Tracker mInstance = null;

    public static Tracker getInstance() {
        if (mInstance == null) {
            mInstance = new Tracker();
        }
        return mInstance;
    }

    // method to track App opens
    public void trackAppOpen(Intent intent) {
        ParseAnalytics.trackAppOpened(intent);
    }

    // method to track photo uploads
    public void trackPhotoUpload(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("photoUpload", dimensions);
    }

    // method to track profile photo uploads
    public void trackProfilePhotoUpload(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("profilePhotoUpload", dimensions);
    }

    // method to track log-ins
    public void trackLogin(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("login", dimensions);
    }

    //method to track logouts
    public void trackLogout(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("logout", dimensions);
    }

    // method to track sign ups
    public void trackSignup(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("signup", dimensions);
    }
}
