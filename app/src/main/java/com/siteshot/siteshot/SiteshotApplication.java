package com.siteshot.siteshot;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by aclissold on 9/25/14.
 */
public class SiteshotApplication extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "1v3hMSVlhYla6NduIkhn76wlZKqH2nHJCLBNSoI0",
                "KO9ARhyVQm4qlknXlnvXQsMGl2oKlCurGZxgPvQp");
    }
}
