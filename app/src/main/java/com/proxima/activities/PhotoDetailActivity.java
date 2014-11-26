package com.proxima.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.proxima.R;
import com.proxima.adapters.ListAdapter;
import com.proxima.models.UserComment;
import com.proxima.utils.ParseProxyObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
// Created by Andrew Clissold, Rachel Glomski, and Jonathan Wong on 11/2/2014.
// Activity to view photos and post comments on them
//
// Recent Version: 11/25/14
public class PhotoDetailActivity extends ActionBarActivity {

    // UI references
    ImageView mImagePhoto;
    TextView mDescription;
    TextView mPostedBy;
    ListView mCommentList;
    Button mPostButton;
    EditText mEditComment;

    // List holding the objectIDs of comments to be displayed
    List<String> commentId;
    // references to for comment operations
    String commentIdPlaceholder;
    ArrayList<UserComment> commentList = new ArrayList<UserComment>();

    ListAdapter adapter;

    // ObjectId of the currently selected photo
    String currentPhoto;

    private static final String TAG = "WOW: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set UI
        setContentView(R.layout.activity_photo_detail);

        mImagePhoto = (ImageView) findViewById(R.id.image_photo);
        mDescription = (TextView) findViewById(R.id.text_description);
        mPostedBy = (TextView) findViewById(R.id.text_posted_by);
        mCommentList = (ListView) findViewById(R.id.comment_list);
        mPostButton = (Button) findViewById(R.id.button_post_comment);
        mEditComment = (EditText) findViewById(R.id.edit_comment);

        // set list adapter
        adapter = new ListAdapter(this, commentList);

        mCommentList.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        ParseProxyObject ppo = (ParseProxyObject) extras.getSerializable("userPhotoObject");
        byte[] data = ppo.getParseFile("photo");
        mDescription.setText(ppo.getString("description"));
        mPostedBy.setText("Posted by: " + ppo.getString("createdBy"));
        currentPhoto = extras.getString("currentObjectId");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        mImagePhoto.setImageBitmap(bitmap);

        reloadAdapter();

        mPostButton.setOnClickListener(new View.OnClickListener(){
            Bitmap bitmap;

            // on click retrieve appropriate data for comment and upload it to Parse
            @Override
            public void onClick(View v) {
                String commentBody = mEditComment.getText().toString();
                if (!TextUtils.isEmpty(commentBody)) {
                    ParseUser user = ParseUser.getCurrentUser();
                    String createdBy = user.getUsername();
                    ParseFile file = (ParseFile) user.get("icon");

                    UserComment newComment = new UserComment();
                    newComment.setComment(commentBody);
                    newComment.setCreatedBy(createdBy);
                    if (file != null) {
                        newComment.setIcon(file);
                    }

                    mEditComment.setText("");

                    byte[] data = new byte[0];
                    try {
                        if ((newComment.getIcon() != null)) {
                            data = newComment.getIcon().getData();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (data.length != 0) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPurgeable = true;
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    } else {
                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    }

                    uploadComment(createdBy, commentBody, bitmap);
                    addComments(v, newComment);
                }
            }
        });
    }

    // method to upload a comment to Parse
    private void uploadComment(String username, String comment, Bitmap bitmap) {
        // retrieve user's icon
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        // create a new ParseFile containing user's icon
        ParseFile file = new ParseFile("userIcon.jpg", data);

        // create a UserComment ParseObject
        final ParseObject object = ParseObject.create("UserComment");
        // assign appropriate data to UserComment
        object.put("createdBy", username);
        object.put("comment", comment);
        object.put("userIcon", file);
        // attempt to save UserComment to Parse
        object.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // if save is successful
                if (e == null) {
                    Log.d(TAG,"nice");
                    // retrieve the objectId of the UserComment
                    commentIdPlaceholder = object.getObjectId();
                    // if there are already comments associated with this photo add the new comment
                    // to the list of comments for the photo to be displayed
                    if (commentId != null) {
                        commentId.add(commentIdPlaceholder);
                    }
                    // retrieve the current UserPhoto to add the objectID of the UserComment to
                    // the UserPhoto's comments list
                    ParseObject point = ParseObject.createWithoutData("UserPhoto", currentPhoto);
                    try {
                        point.fetch();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    // if the UserPhoto has existing comments add UserComment to it's list
                    if (commentId != null) {
                        point.put("userComments", commentId);
                        point.saveInBackground();
                    }
                    // if the UserPhoto does not have any comments yet, create a new list, add
                    // the current comment to the list and save the list to the current UserPhoto
                    else if (commentId == null){
                        List listA = new ArrayList<String>();
                        listA.add(commentIdPlaceholder);
                        point.put("userComments", listA);
                        point.saveInBackground();
                        commentId = listA;
                    }
                } else {
                    Log.d(TAG,"less nice", e);
                }
            }
        });
    }

    // method to update the listview adapter
    public void addComments(View v, UserComment comment) {
        // add a new comment to the list of comments to be displayed
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

    // reload the comment data for the list adapter to update the comment list
    public void reloadAdapter(){
        // get the current UserPhoto object
        ParseObject current = ParseObject.createWithoutData("UserPhoto", currentPhoto);
        try {
            current.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // get the list of user comments associated with the current UserPhoto
        commentId = current.getList("userComments");

        // if the current list of comment id's is not null, retrieve the user comments
        // from Parse and update the list of user comments used by the adapter with any new comments added
        if (commentId != null) {
            ParseQuery<UserComment> query = UserComment.getQuery();
            query.whereContainedIn("objectId", commentId);
            query.orderByAscending("createdAt");
            query.findInBackground(new FindCallback<UserComment>() {
                @Override
                public void done(List<UserComment> resultUserComments, ParseException e) {
                    if (e == null) {
                        commentList.addAll(resultUserComments);
                    } else {
                        Log.e(TAG, "error retrieving user photos:");
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
