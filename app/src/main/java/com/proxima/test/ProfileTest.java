package com.proxima.test;

import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseUser;
import com.proxima.activities.ProfileActivity;

/**
 * Created by aclissold on 9/25/14.
 */
public class ProfileTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    private ProfileActivity mProfileActivity;

    public ProfileTest() {
        super(ProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mProfileActivity = getActivity();
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
