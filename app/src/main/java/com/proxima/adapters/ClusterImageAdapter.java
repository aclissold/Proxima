package com.proxima.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.proxima.R;
import com.proxima.activities.ClusterViewActivity;
import com.proxima.activities.PhotoDetailActivity;
import com.proxima.utils.ParseProxyObject;
import com.proxima.utils.PhotoUtils;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

//
// Created by Andrew Clissold, Rachel Glomski, and Jonathan Wong on 11/2/2014.
// Adapts PhotoUtils' List of UserPhotos, to populate a grid view with UserPhotos in a
// cluster. Also displays a badge corresponding to whether a photo is undiscovered, discovered,
// or recently discovered.
//
// Recent Version: 11/26/14
public class ClusterImageAdapter extends BaseAdapter {
    //references for UI
    private final String TAG = getClass().getName();
    private int mWidth, mHeight;
    private static final float SIZE_DP = 90.0f;
    private Context mContext;
    private LayoutInflater layoutInflater;

    // reference to the ClusterViewActivity
    private ClusterViewActivity count;

    // flag to determine if a photo should be viewable
    private boolean unlocked;

    public ClusterImageAdapter(Context c, ClusterViewActivity counter) {
        mContext = c;
        final float scale = c.getResources().getDisplayMetrics().density;
        layoutInflater = LayoutInflater.from(mContext);

        // Adjust the width and height "constants" based on screen density.
        mWidth = (int) (SIZE_DP * scale + 0.5f);
        mHeight = mWidth;


        this.count = counter;
    }

    // method to return the number of photos in a cluster for use in the
    // grid view adapter
    public int getCount() {
        return count.getCount();
    }

    // method to populate the grid view
    public Object getItem(int position) {
        return null;
    }

    // method to populate the grid view
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // references for the grid view UI
        View grid;
        ImageView imageView;
        ImageView imageView2;

        // references to cluster data
        String currentUser;
        List<String> unlockedUser;
        String[] newlyDiscovered;

        // variable to determine if newly discovered badge should be shown with a photo
        boolean newBadge = false;

        // variable to determine if a photo should be displayed
        boolean unlockedFlag = false;

        if (convertView == null) {
            grid = new View(mContext);
            grid = layoutInflater.inflate(R.layout.row_grid, null);
            grid.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
        }
        else {
            grid = (View) convertView;
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
        newlyDiscovered = count.pushDiscovered();

        // loop thru a photo's unlocked user list and determine if the current user in
        // in the list to determine if a photo should be displayed
        for(String s : unlockedUser){
            if (currentUser.equals(s)) {
                unlockedFlag = true;
                // if the current user is in the unlocked list determine if the photo has been
                // recently unlocked
                for (String t : newlyDiscovered){
                    for (String r : count.pushArray()){
                        if (t.equals(r)){
                            newBadge = true;
                        }
                    }
                }
            }
        }
        // if the photo is discovered then display the photo with the appropriate badge
        if (unlockedFlag && !newBadge) {
            ParseFile file = object.getParseFile("photo");
            byte[] data = new byte[0];
            try {
                data = file.getData();
            } catch (ParseException e) {
                Log.e(TAG, "could not get data from ParseFile:");
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            imageView = (ImageView)grid.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false));
            imageView.setImageBitmap(bitmap);

            Bitmap bitmap2 = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.old);;

            imageView2 = (ImageView)grid.findViewById(R.id.badge);
            imageView2.setImageBitmap(bitmap2);
        }
        // if the photo is discovered and it is newly discovered display the photo
        // and set the appropriate badge
        else if (unlockedFlag && newBadge) {
            ParseFile file = object.getParseFile("photo");
            byte[] data = new byte[0];
            try {
                data = file.getData();
            } catch (ParseException e) {
                Log.e(TAG, "could not get data from ParseFile:");
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            imageView = (ImageView)grid.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false));
            imageView.setImageBitmap(bitmap);

            Bitmap bitmap2 = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.discover);

            imageView2 = (ImageView)grid.findViewById(R.id.badge);
            imageView2.setImageBitmap(bitmap2);
        }
        // if the photo is undiscovered display the undiscovered icon and set the appropriate badge
        else {
            imageView = (ImageView)grid.findViewById(R.id.image);
            Bitmap bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.undisc_thumb);;
            imageView.setImageBitmap(bitmap);

            Bitmap bitmap2 = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.undiscover);

            imageView2 = (ImageView)grid.findViewById(R.id.badge);
            imageView2.setImageBitmap(bitmap2);
        }

        // set the on click listener for the photo
        imageView.setOnClickListener(new ImageOnClickListener(object));
        return grid;
    }

    // onClickListener for a photo
    class ImageOnClickListener implements View.OnClickListener {
        // references for selected UserPhoto data
        private ParseProxyObject ppo;
        private String selectedUserPhoto;
        private String currentUser;

        // references to determine action on a photo click
        private ParseObject unlockCheck;
        List<String> unlockedUser;

        // assign references corresponding to clicked image
        public ImageOnClickListener(ParseObject userPhoto)
        {
            ppo = new ParseProxyObject(userPhoto);
            selectedUserPhoto = userPhoto.getObjectId();
            unlockCheck = userPhoto;
        }

        // on click determine if a user should be taken to the photo detail view
        public void onClick(View v)
        {
            // get the current username
            currentUser = ParseUser.getCurrentUser().getUsername();
            // check for the current username in the selected photo's unlocked list
            unlockedUser = unlockCheck.getList("unlocked");
            for(String s : unlockedUser){
                if (currentUser.equals(s)) {
                    unlocked = true;
                }
            }

            // if current user is in the unlocked list launch the photo detail view with the
            // appropriate data, otherwise show a toast indicating undiscovered photo
            if (unlocked) {
                Intent photoDetailIntent = new Intent(v.getContext(), PhotoDetailActivity.class);
                photoDetailIntent.putExtra("userPhotoObject", ppo);
                photoDetailIntent.putExtra("currentObjectId", selectedUserPhoto);
                unlocked = false;
                v.getContext().startActivity(photoDetailIntent);
            }
            else {
                Toast toast = Toast.makeText(mContext, "This photo has not been discovered yet", LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
