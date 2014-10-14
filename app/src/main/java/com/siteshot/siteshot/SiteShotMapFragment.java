package com.siteshot.siteshot;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * A placeholder fragment containing the map view.
 */
public class SiteShotMapFragment extends Fragment {

    MapView mapFragment;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SiteShotMapFragment newInstance(int sectionNumber) {
        SiteShotMapFragment fragment = new SiteShotMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public SiteShotMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.siteshot_map_fragment, container, false);


        // Set up the Map fragment.
        mapFragment = (MapView) rootView.findViewById(R.id.mapView);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();

        // Enable the current location "blue dot"
        mapFragment.getMap().setMyLocationEnabled(true);
        // Set up the camera change handler
        mapFragment.getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                // When the camera changes, update the query
                // doMapQuery();
            }
        });

        return rootView;
    }



}
