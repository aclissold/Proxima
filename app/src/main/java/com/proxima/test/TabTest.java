package com.proxima.test;

import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseUser;
import com.proxima.activities.TabActivity;

/**
 * Created by aclissold on 9/23/14.
 */
public class TabTest extends ActivityInstrumentationTestCase2<TabActivity> {

    public TabTest() {
        super(TabActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        // Create the activity, to call Parse.initialize();
        super.setUp();
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
