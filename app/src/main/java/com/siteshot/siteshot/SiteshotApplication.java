package com.siteshot.siteshot;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.siteshot.siteshot.models.UserPhoto;

import java.util.List;

/**
 * Created by aclissold on 9/25/14.
 */
public class SiteshotApplication extends Application {

    private final String TAG = getClass().getName();

    private List<UserPhoto> userPhotos;

    public List<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    private void downloadUserPhotos() {
            ParseQuery<UserPhoto> query = UserPhoto.getQuery();
            query.findInBackground(new FindCallback<UserPhoto>() {
                @Override
                public void done(List<UserPhoto> resultUserPhotos, ParseException e) {
                    if (e == null) {
                        userPhotos = resultUserPhotos;
                        Log.d(TAG, "got photos");
                        Log.d(TAG, "" + userPhotos.size());
                    } else {
                        Log.e(TAG, "error retrieving user photos:");
                        e.printStackTrace();
                    }
                }
            });
    }
    @Override
    public void onCreate() {
        Parse.initialize(this, "1v3hMSVlhYla6NduIkhn76wlZKqH2nHJCLBNSoI0",
                "KO9ARhyVQm4qlknXlnvXQsMGl2oKlCurGZxgPvQp");
        ParseACL.setDefaultACL(new ParseACL(), true);

        downloadUserPhotos();

    }
}

