package com.siteshot.siteshot.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by rachel on 10/9/14. Data model for a post.
 */
@ParseClassName("UserPhoto")
public class UserPhoto extends ParseObject {
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation (ParseGeoPoint value) {
        put("location", value);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser (ParseUser value) {
        put("user", value);
    }

    public ParseFile getPhoto() {
        return getParseFile("photo");
    }

    public void setPhoto (ParseFile value) {
        put("photo", value);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String value) {
        put("text", value);
    }

    public static ParseQuery<UserPhoto> getQuery() {
        return ParseQuery.getQuery(UserPhoto.class);
    }


}
