package com.proxima.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.proxima.R;
import com.proxima.adapters.ImageAdapter;
import com.proxima.utils.PhotoUtils;
import com.proxima.utils.Tracker;

import java.io.File;
import java.io.IOException;

/**
 * Created by Andrew Clissold, Rachel Glomski, Jon Wong on 9/25/14.
 * Activity that displays user profile, user album, user settings
 */
public class ProfileActivity extends Activity {

    public TextView mUsernameView;
    public ImageView mUserIcon;
    private final String TAG = ProfileActivity.class.getName();
    private int mWidth, mHeight;
    private static final float SIZE_DP = 90.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final float scale = this.getResources().getDisplayMetrics().density;

        // Adjust the width and height "constants" based on screen density.
        mWidth = (int) (SIZE_DP * scale + 0.5f);
        mHeight = mWidth;

        setContentView(R.layout.activity_profile);

        mUsernameView = (TextView) findViewById(R.id.profile_username);
        mUserIcon = (ImageView) findViewById(R.id.profile_user_icon);
        mUsernameView.setText(ParseUser.getCurrentUser().getUsername());
        GridView photoGrid = (GridView) findViewById(R.id.profile_photo_grid);
        photoGrid.setAdapter(new ImageAdapter(this));

        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Placeholder for activity photo view
                Toast.makeText(ProfileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        setUserIcon();
    }

    private void setUserIcon() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = (ParseFile) user.get("icon");

        if (file != null) {
            try {
                byte[] data = file.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                mUserIcon.setImageBitmap(bitmap);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // do not correct rotation for native camera
        boolean profileRotate = false;
        if (resultCode == RESULT_OK) {
            String photoPath = PhotoUtils.getInstance().getCurrentPhotoPath();
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, new BitmapFactory.Options());
            Bitmap rotatedBitmap = PhotoUtils.getInstance().uploadProfilePhoto(Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false), profileRotate);
            mUserIcon.setImageBitmap(rotatedBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            // finishes profile activity and returns user to tab activity
            case R.id.action_return:
                finish();
                return true;

            // logout the user and redirect to login activity. finishes profile activity
            // so the user will not have previous user's profile info displayed
            case R.id.action_logout:
                Tracker.getInstance().trackLogout(ParseUser.getCurrentUser().getUsername());
                ParseUser.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoUtils.getInstance().createPhotoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
