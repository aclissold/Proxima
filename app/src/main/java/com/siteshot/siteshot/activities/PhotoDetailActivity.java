package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.utils.ParseProxyObject;
import com.siteshot.siteshot.utils.PhotoUtils;

public class PhotoDetailActivity extends Activity {

    ImageView mImagePhoto;
    TextView mDescription;
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mImagePhoto = (ImageView) findViewById(R.id.image_photo);
        mDescription = (TextView) findViewById(R.id.text_description);
        Bundle extras = getIntent().getExtras();
        ParseProxyObject ppo = (ParseProxyObject) extras.getSerializable("userPhotoObject");
        byte[] data = ppo.getParseFile("photo");
        mDescription.setText(ppo.getString("description"));

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        mImagePhoto.setImageBitmap(bitmap);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
