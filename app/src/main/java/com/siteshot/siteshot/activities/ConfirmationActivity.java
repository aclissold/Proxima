package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseGeoPoint;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.utils.PhotoUtils;

public class ConfirmationActivity extends Activity {

    Button postButton;
    Button cancelButton;
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        postButton = (Button) findViewById(R.id.button_post);
        cancelButton = (Button) findViewById(R.id.button_cancel);


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmationIntent = getIntent();
                Location location = (Location) confirmationIntent.getExtras().get("location");
                Boolean rotateFlag = confirmationIntent.getExtras().getBoolean("rotateFlag");
                Log.d(TAG, rotateFlag.toString());
                ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                PhotoUtils.getInstance().uploadPhoto(confirmationIntent.getExtras().getByteArray("data"), geoPoint,
                rotateFlag);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirmation, menu);
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
