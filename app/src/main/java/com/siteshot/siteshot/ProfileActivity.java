package com.siteshot.siteshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;


public class ProfileActivity extends Activity {

    public TextView mUsernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        mUsernameView = (TextView) findViewById(R.id.profile_username);
        mUsernameView.setText(ParseUser.getCurrentUser().getUsername());
        GridView photoGrid = (GridView) findViewById(R.id.profile_photo_grid);
        photoGrid.setAdapter(new ImageAdapter(this));

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Placeholder for activity photo view
                Toast.makeText(ProfileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

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
                ParseUser.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
