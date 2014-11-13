package com.proxima;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.proxima.activities.TabActivity;
import com.proxima.models.UserComment;
import com.proxima.models.UserPhoto;
import com.proxima.utils.PhotoUtils;

/**
 * Created by aclissold on 9/25/14.
 */
public class ProximaApplication extends Application {

    private final String TAG = getClass().getName();

    @Override
    public void onCreate() {
        ParseObject.registerSubclass(UserPhoto.class);
        ParseObject.registerSubclass(UserComment.class);
        Parse.initialize(this, "1v3hMSVlhYla6NduIkhn76wlZKqH2nHJCLBNSoI0",
                "KO9ARhyVQm4qlknXlnvXQsMGl2oKlCurGZxgPvQp");

        PhotoUtils.getInstance().downloadUserPhotos();
        PushService.setDefaultPushCallback(this, TabActivity.class);
        ParseUser user = ParseUser.getCurrentUser();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (user != null && installation.get("user") == null) {
            installation.put("user", user);
            installation.saveInBackground();
        }
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}

