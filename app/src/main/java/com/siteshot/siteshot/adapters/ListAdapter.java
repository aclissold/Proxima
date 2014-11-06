package com.siteshot.siteshot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siteshot.siteshot.R;
import com.siteshot.siteshot.models.UserComment;

import java.util.List;

/**
 * Created by rachel on 10/30/14.
 */
public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<UserComment> mCommentList;

    public ListAdapter(Context context, List<UserComment> comments) {
        mInflater = LayoutInflater.from(context);
        mCommentList = comments;
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
        //holder.icon.setImageBitmap(Bit);
        holder.commenter.setText("TESTING");
        holder.comment.setText("WOW");

        return view;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView commenter, comment;
    }
}
