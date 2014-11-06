package com.siteshot.siteshot;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.siteshot.siteshot.models.UserComment;
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
        ParseObject.registerSubclass(UserComment.class);
        Parse.initialize(this, "1v3hMSVlhYla6NduIkhn76wlZKqH2nHJCLBNSoI0",
                "KO9ARhyVQm4qlknXlnvXQsMGl2oKlCurGZxgPvQp");

        PhotoUtils.getInstance().downloadUserPhotos();
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}

