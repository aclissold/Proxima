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
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.activities.ClusterViewActivity;
import com.siteshot.siteshot.utils.PhotoUtils;

import java.util.List;

/**
 * Adapts PhotoUtils' List of UserPhotos.
 */
public class ClusterImageAdapter extends BaseAdapter {

    private final String TAG = getClass().getName();
    private int mWidth, mHeight;
    private static final float SIZE_DP = 90.0f;
    private Context mContext;
    private ClusterViewActivity count;
    public ClusterImageAdapter(Context c, ClusterViewActivity counter) {
        mContext = c;
        final float scale = c.getResources().getDisplayMetrics().density;

        // Adjust the width and height "constants" based on screen density.
        mWidth = (int) (SIZE_DP * scale + 0.5f);
        mHeight = mWidth;
        this.count = counter;
    }

    public int getCount() {
        int watch = count.getCount();

        return count.getCount();
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
        String currentUser;
        List<String> unlockedUser;
        int unlockedSize;
        boolean unlockedFlag = false;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        // Retrieve the photo data from the UserPhoto instance.
        ParseObject object = null;
        try {
            object = PhotoUtils.getInstance().getClusterPhotos(count.pushArray()).get(position);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentUser = count.getUser();
        unlockedUser = object.getList("unlocked");
        unlockedSize = unlockedUser.size();

        for(String s : unlockedUser){

            if (currentUser.equals(s)) {
                unlockedFlag = true;
            }
        }
        if (unlockedFlag) {
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

            imageView.setImageBitmap(bitmap);
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.locked_photo);;
            imageView.setImageBitmap(bitmap);
        }

        return imageView;
    }

}
