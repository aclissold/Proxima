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
import com.proxima.R;
import com.proxima.activities.ClusterViewActivity;
import com.proxima.activities.PhotoDetailActivity;
import com.proxima.utils.ParseProxyObject;
import com.proxima.utils.PhotoUtils;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Adapts PhotoUtils' List of UserPhotos.
 */
public class ClusterImageAdapter extends BaseAdapter {

    private final String TAG = getClass().getName();
    private int mWidth, mHeight;
    private static final float SIZE_DP = 90.0f;
    private Context mContext;
    private ClusterViewActivity count;
    private LayoutInflater layoutInflater;
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
        View grid;
        ImageView imageView;
        ImageView imageView2;

        String currentUser;
        List<String> unlockedUser;
        String[] newlyDiscovered;
        boolean newBadge = false;

        int unlockedSize;
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
        unlockedSize = unlockedUser.size();

        for(String s : unlockedUser){

            if (currentUser.equals(s)) {
                unlockedFlag = true;
                for (String t : newlyDiscovered){
                    for (String r : count.pushArray()){
                        if (t.equals(r)){
                            newBadge = true;
                        }
                    }
                }
            }
        }
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

        else {
            imageView = (ImageView)grid.findViewById(R.id.image);
            Bitmap bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.undisc_thumb);;
            imageView.setImageBitmap(bitmap);

            Bitmap bitmap2 = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.undiscover);

            imageView2 = (ImageView)grid.findViewById(R.id.badge);
            imageView2.setImageBitmap(bitmap2);
        }
        unlocked = unlockedFlag;

        imageView.setOnClickListener(new ImageOnClickListener(object));
        return grid;
    }
    class ImageOnClickListener implements View.OnClickListener {
        private ParseProxyObject ppo;
        private String selectedUserPhoto;

        public ImageOnClickListener(ParseObject userPhoto)
        {
            ppo = new ParseProxyObject(userPhoto);
            selectedUserPhoto = userPhoto.getObjectId();

        }

        public void onClick(View v)
        {
            if (unlocked) {
                Intent photoDetailIntent = new Intent(v.getContext(), PhotoDetailActivity.class);
                photoDetailIntent.putExtra("userPhotoObject", ppo);
                photoDetailIntent.putExtra("currentObjectId", selectedUserPhoto);
                v.getContext().startActivity(photoDetailIntent);
            }
            else {
                Toast toast = Toast.makeText(mContext, "This photo has not been discovered yet", LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
