package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.utils.PhotoUtils;

public class ConfirmationActivity extends Activity {

    Button mPostButton;
    Button mCancelButton;
    EditText mDescriptionEditText;
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mPostButton = (Button) findViewById(R.id.button_post);
        mCancelButton = (Button) findViewById(R.id.button_cancel);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_text);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered description.
                String description = mDescriptionEditText.getText().toString();
                Log.d(TAG, "Description: " + description); // TODO: associate with UserPhoto

                // Get the photo components sent over in extras.
                Bundle extras = getIntent().getExtras();
                Location location = (Location) extras.get("location");
                Boolean rotateFlag = extras.getBoolean("rotateFlag");
                byte[] data = extras.getByteArray("data");
                ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                // Upload the photo.
                // TODO: upload with description
                PhotoUtils.getInstance().uploadPhoto(data, geoPoint, rotateFlag, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Upload succeeded; dismiss activity.
                            finish();
                        } else {
                            // Error occurred; display it and don't dismiss.
                            CharSequence message = getString(R.string.error_photo_upload_failed);
                            Context context = getApplicationContext();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Log.e(TAG, message.toString());
                        }
                    }
                });
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
