package com.siteshot.siteshot.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.models.UserComment;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by rachel on 10/30/14.
 */
public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<UserComment> mCommentList;
    private int mWidth, mHeight;
    private static final float SIZE_DP = 50.0f;
    private static final String TAG = "WOW: ";

    public ListAdapter(Context context, List<UserComment> comments) {
        mInflater = LayoutInflater.from(context);
        mCommentList = comments;

        final float scale = context.getResources().getDisplayMetrics().density;

        // Adjust the width and height "constants" based on screen density.
        mWidth = (int) (SIZE_DP * scale + 0.5f);
        mHeight = mWidth;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        Bitmap bitmap;
        
        if(convertView == null) {
            view = mInflater.inflate(R.layout.comment_list, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.comment_icon);
            holder.commenter = (TextView)view.findViewById(R.id.comment_username);
            holder.comment = (TextView)view.findViewById(R.id.comment_comment);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        UserComment userComment = mCommentList.get(position);
        holder.commenter.setText(userComment.getCreatedBy());
        holder.comment.setText(userComment.getComment());

        byte[] data = new byte[0];
        try {
            if ((userComment.getIcon() != null)) {
                data = userComment.getIcon().getData();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (data.length != 0) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inPurgeable = true;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.icon.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false));
        } else {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            bitmap = Bitmap.createBitmap(mWidth, mHeight, conf);
            holder.icon.setImageBitmap(bitmap);
        }


        return view;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView commenter, comment;
    }


}
