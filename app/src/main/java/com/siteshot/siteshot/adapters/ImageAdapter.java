package com.siteshot.siteshot.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.siteshot.siteshot.utils.PhotoUtils;

/**
 * Adapts PhotoUtils' List of UserPhotos.
 */
public class ImageAdapter extends BaseAdapter {
    private final String TAG = getClass().getName();

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return PhotoUtils.getInstance().getUserPhotos().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        // Retrieve the photo data from the UserPhoto instance.
        ParseObject object = PhotoUtils.getInstance().getUserPhotos().get(position);
        ParseFile file = object.getParseFile("photo");
        byte[] data = new byte[0];
        try {
            data = file.getData();
        } catch (ParseException e) {
            Log.e(TAG, "could not get data from ParseFile:");
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }

}