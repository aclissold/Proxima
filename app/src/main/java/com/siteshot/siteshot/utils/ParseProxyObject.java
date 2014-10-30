package com.siteshot.siteshot.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.HashMap;

public class ParseProxyObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Object> values = new HashMap<String, Object>();
    private final String TAG = "WOW: ";

    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }
    public ParseProxyObject(ParseObject object) {
// Loop the keys in the ParseObject
        for(String key : object.keySet()) {
            @SuppressWarnings("rawtypes")
            Class classType = object.get(key).getClass();
            if(classType == byte[].class || classType == String.class ||
                    classType == Integer.class || classType == Boolean.class) {
                values.put(key, object.get(key));
            } else if(classType == ParseUser.class) {
                ParseProxyObject parseUserObject = new ParseProxyObject((ParseObject)object.get(key));
                values.put(key, parseUserObject);
            } else if(classType == ParseFile.class) {
                Log.e(TAG, "Wow you're a file " + key);
                ParseFile parseFile = object.getParseFile(key);

                byte[] data = new byte[0];
                try {
                    data = parseFile.getData();
                } catch (ParseException e) {
                    Log.e(TAG, "could not get data from ParseFile:");
                    e.printStackTrace();
                }

                values.put(key, data);
            } else {
// You might want to add more conditions here, for embedded ParseObject, ParseFile, etc.
            }
        }
    }
    public String getString(String key) {
        if(has(key)) {
            return (String) values.get(key);
        } else {
            return "";
        }
    }
    public int getInt(String key) {
        if(has(key)) {
            return (Integer)values.get(key);
        } else {
            return 0;
        }
    }
    public Boolean getBoolean(String key) {
        if(has(key)) {
            return (Boolean)values.get(key);
        } else {
            return false;
        }
    }
    public byte[] getBytes(String key) {
        if(has(key)) {
            return (byte[])values.get(key);
        } else {
            return new byte[0];
        }
    }
    public ParseProxyObject getParseUser(String key) {
        if(has(key)) {
            return (ParseProxyObject) values.get(key);
        } else {
            return null;
        }
    }
    public Boolean has(String key) {
        return values.containsKey(key);
    }

    public Bitmap getBitmap(String key) {
        if(has(key)) {
            return (Bitmap)values.get(key);
        } else {
            return null;
        }
    }

    public byte[] getParseFile(String key) {
        if(has(key)) {
            Log.e(TAG, "wowie" + key);
            return (byte[]) values.get(key);
        } else {
            Log.e(TAG, "WHYYY");
            return null;
        }
    }

}
