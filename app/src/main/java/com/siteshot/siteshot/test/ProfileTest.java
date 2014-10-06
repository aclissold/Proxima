package com.siteshot.siteshot.test;

import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseUser;
import com.siteshot.siteshot.activities.ProfileActivity;

/**
 * Created by aclissold on 9/25/14.
 */
public class ProfileTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    ProfileActivity mProfileActivity;

    public ProfileTest() {
        super(ProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mProfileActivity = getActivity(); // initializes Parse
        if (ParseUser.getCurrentUser() == null) {
            ParseUser.logIn("test", "test");
        }
    }

    public void testUsernameTextFieldSet() {
        CharSequence expected = ParseUser.getCurrentUser().getUsername();
        CharSequence actual = mProfileActivity.mUsernameView.getText();
        assertEquals(expected, actual);
    }
}
