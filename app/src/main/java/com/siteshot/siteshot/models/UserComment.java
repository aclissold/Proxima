package com.siteshot.siteshot.models;

import android.widget.ImageView;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by rachel on 10/30/14.
 */

@ParseClassName("UserComment")
public class UserComment extends ParseObject {

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public String getComment() {
        return getString("comment");
    }

    public void setComment (String comment) {
        put("comment", comment);
    }

    public String getCreatedBy() {
        return getString("createdBy");
    }

    public void setCreatedBy(String createdBy) {
        put("createdBy", createdBy);
    }

    public void setUser (ParseUser value) {
        put("user", value);
    }

    public void setIcon (ParseFile userIcon) {
        put("userIcon", userIcon);
    }

    public ParseFile getIcon() {
        return getParseFile("userIcon");
    }

    public static ParseQuery<UserComment> getQuery() {
        return ParseQuery.getQuery(UserComment.class);
    }
}
