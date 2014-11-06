package com.siteshot.siteshot.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.fragments.SiteShotMapFragment;

/**
 * Custom Info Window Adapter for markers
 * Created by Jonathan on 11/2/2014.
 */
public class CustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
    private final String TAG = getClass().getName();
    private SiteShotMapFragment owner;
    public CustomAdapterForItems(SiteShotMapFragment owner) {
        this.owner = owner;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        // Use the default frame.
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        View view = owner.getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
        ParseFile file = owner.mClickedClusterItem.getUserPhoto().getPhoto();
        Bitmap bitmap = null;
        try {
            byte[] data = file.getData();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (ParseException e) {
            Log.e(TAG, "error getting user photo bytes");
            e.printStackTrace();
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return view;
    }
}
