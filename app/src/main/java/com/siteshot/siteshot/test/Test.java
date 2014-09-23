package com.siteshot.siteshot.test;

import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseUser;
import com.siteshot.siteshot.TabActivity;

/**
 * Created by aclissold on 9/23/14.
 */
public class Test extends ActivityInstrumentationTestCase2<TabActivity> {

    public Test() {
        super(TabActivity.class);
    }

    @Override
    public void setUp() {
        // Create the activity, to call Parse.initialize();
        getActivity();
    }

    public void testTestCasesAreFunctional() throws Exception {
        assertTrue(true);
    }

    public void testParseIntegration() throws Exception {
        ParseUser.logOut();
        ParseUser.logIn("test", "test");
        assertNotNull(ParseUser.getCurrentUser());
    }
}
