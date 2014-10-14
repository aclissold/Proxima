package com.siteshot.siteshot;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.siteshot.siteshot.models.UserPhoto;
import com.siteshot.siteshot.utils.PhotoUtils;

/**
 * Created by aclissold on 9/25/14.
 */
public class SiteShotApplication extends Application {

    private final String TAG = getClass().getName();

    @Override
    public void onCreate() {
        ParseObject.registerSubclass(UserPhoto.class);
        Parse.initialize(this, "1v3hMSVlhYla6NduIkhn76wlZKqH2nHJCLBNSoI0",
                "KO9ARhyVQm4qlknXlnvXQsMGl2oKlCurGZxgPvQp");
        ParseACL.setDefaultACL(new ParseACL(), true);

        PhotoUtils.getInstance().downloadUserPhotos();
    }
}

