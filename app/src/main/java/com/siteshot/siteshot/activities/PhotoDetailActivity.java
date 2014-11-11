package com.siteshot.siteshot.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.adapters.ListAdapter;
import com.siteshot.siteshot.models.UserComment;
import com.siteshot.siteshot.utils.ParseProxyObject;
import com.siteshot.siteshot.utils.PhotoUtils;

import java.util.ArrayList;

public class PhotoDetailActivity extends Activity {

    ImageView mImagePhoto;
    TextView mDescription;
    TextView mPostedBy;
    ListView mCommentList;
    Button mPostButton;
    EditText mEditComment;
    ArrayList<UserComment> commentList = new ArrayList<UserComment>();
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mImagePhoto = (ImageView) findViewById(R.id.image_photo);
        mDescription = (TextView) findViewById(R.id.text_description);
        mPostedBy = (TextView) findViewById(R.id.text_posted_by);
        mCommentList = (ListView) findViewById(R.id.comment_list);
        mPostButton = (Button) findViewById(R.id.button_post_comment);
        mEditComment = (EditText) findViewById(R.id.edit_comment);

//        UserComment test = new UserComment();
//        test.setComment("WOWIE");
//        test.setCreatedBy("ME");
//
//        commentList.add(0, test);
//        commentList.add(1, test);
//        commentList.add(2, test);
//        commentList.add(3, test);
//        commentList.add(4, test);


        adapter = new ListAdapter(this, commentList);

        mCommentList.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        ParseProxyObject ppo = (ParseProxyObject) extras.getSerializable("userPhotoObject");
        byte[] data = ppo.getParseFile("photo");
        mDescription.setText(ppo.getString("description"));
        mPostedBy.setText("Posted by: " + ppo.getString("createdBy"));


        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        mImagePhoto.setImageBitmap(bitmap);

        mPostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String commentBody = mEditComment.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                String createdBy = user.getUsername();
                ParseFile file = (ParseFile) user.get("icon");

                UserComment newComment = new UserComment();
                newComment.setComment(commentBody);
                newComment.setCreatedBy(createdBy);
                if (file != null) {
                    newComment.setIcon(file);
                }
                addComments(v, newComment);
                mEditComment.setText("");
            }
        });


    }

    public void addComments(View v, UserComment comment) {
        commentList.add(comment);
        adapter.notifyDataSetChanged();
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
