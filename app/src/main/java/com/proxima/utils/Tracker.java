package com.proxima.utils;

import android.content.Intent;

import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aclissold on 11/11/14.
 *
 * Abstracts ParseAnalytics method calls to one-liners.
 */
public class Tracker {

    private static Tracker mInstance = null;

    public static Tracker getInstance() {
        if (mInstance == null) {
            mInstance = new Tracker();
        }
        return mInstance;
    }

    public void trackAppOpen(Intent intent) {
        ParseAnalytics.trackAppOpened(intent);
    }

    public void trackPhotoUpload(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("photoUpload", dimensions);
    }

    public void trackProfilePhotoUpload(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("profilePhotoUpload", dimensions);
    }

    public void trackLogin(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("login", dimensions);
    }

    public void trackLogout(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("logout", dimensions);
    }

    public void trackSignup(String username) {
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("username", username);
        ParseAnalytics.trackEvent("signup", dimensions);
    }

}
