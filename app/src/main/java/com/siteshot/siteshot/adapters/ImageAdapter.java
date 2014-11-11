package com.siteshot.siteshot.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.siteshot.siteshot.activities.PhotoDetailActivity;
import com.siteshot.siteshot.utils.ParseProxyObject;
import com.siteshot.siteshot.utils.PhotoUtils;

import java.io.Serializable;

/**
 * Adapts PhotoUtils' List of UserPhotos.
 */
public class ImageAdapter extends BaseAdapter {

    private final String TAG = getClass().getName();
    private int mWidth, mHeight;
    private static final float SIZE_DP = 90.0f;
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
        final float scale = c.getResources().getDisplayMetrics().density;

        // Adjust the width and height "constants" based on screen density.
        mWidth = (int) (SIZE_DP * scale + 0.5f);
        mHeight = mWidth;
    }

    public int getCount() {
        int watch = PhotoUtils.getInstance().getUserPhotos().size();
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
            imageView.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
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

        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false));

        imageView.setOnClickListener(new ImageOnClickListener(object));

        return imageView;
    }

    class ImageOnClickListener implements View.OnClickListener {
        private ParseProxyObject ppo;

        public ImageOnClickListener(ParseObject userPhoto)
        {
            ppo = new ParseProxyObject(userPhoto);
        }

        public void onClick(View v)
        {
            Intent photoDetailIntent = new Intent(v.getContext(), PhotoDetailActivity.class);
            photoDetailIntent.putExtra("userPhotoObject", ppo);
            v.getContext().startActivity(photoDetailIntent);
        }
    }


}