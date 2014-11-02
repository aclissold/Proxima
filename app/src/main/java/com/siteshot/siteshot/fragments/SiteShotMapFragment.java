package com.siteshot.siteshot.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.siteshot.siteshot.R;
import com.siteshot.siteshot.activities.ClusterViewActivity;
import com.siteshot.siteshot.activities.TabActivity;
import com.siteshot.siteshot.models.SiteShotClusterItem;
import com.siteshot.siteshot.models.UserPhoto;
import com.siteshot.siteshot.utils.PhotoUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import java.text.ParseException;

/**
 * Created by Andrew Clissold, Rachel Glomski, Jon Wong on 10/12/14.
 * Fragment containing the map view. Will display a map centered around user's location. Will
 * display photos taken by the user as well as other SiteShot users.
 *
 * Adapted from code from Parse.com AnyWall tutorial
 * https://parse.com/tutorials/anywall-android
 */
public class SiteShotMapFragment extends Fragment implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private final String TAG = TabActivity.class.getName();

    MapView mapFragment;

    // For cluster item adapters.
    public Cluster<SiteShotClusterItem> mClickedCluster;
    public SiteShotClusterItem mClickedClusterItem;

    public ArrayList<String> clusterContents = new ArrayList<String>();
    public int mClusterSize;
    public String objectID;

    public boolean singleMark;
    public boolean clusterMark;

    public boolean unlockFlag;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final int MILLISECONDS_PER_SECOND = 1000;
    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;
    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;
    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // Represents the circle around a map
    private Circle mapCircle;

    // Fields for the map radius in feet
    private float radius = 50;
    private float lastRadius;

    // Fields for helping process map and location changes
    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
    private int mostRecentMapUpdate;
    private boolean hasSetUpInitialLocation;
    private String selectedPostObjectId;
    private Location lastLocation;
    private Location currentLocation;

    private LocationRequest locationRequest;
    private LocationClient locationClient;

    /*
 * Constants for handling location results
 */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;

    private ClusterManager<SiteShotClusterItem> mClusterManager;
    private ClusterListener mClusterListener;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "Map";

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
        // Set up location services.

        View rootView = inflater.inflate(R.layout.siteshot_map_fragment, container, false);

        MapsInitializer.initialize(getActivity());
        // Set up the Map fragment.
        mapFragment = (MapView) rootView.findViewById(R.id.mapView);

        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();

        // Configure the map.
        GoogleMap googleMap = mapFragment.getMap();


        // sets up the thumbnail preview for a clicked single item
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                           // Use default InfoWindow frame
                                           @Override
                                           public View getInfoWindow(Marker arg0) {
                                               return null;
                                           }

                                           // Defines the contents of the InfoWindow
                                           @Override
                                           public View getInfoContents(Marker arg0) {

                                               // Getting view from the layout file info_window_layout
                                               View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

                                               if (singleMark && unlockFlag) {
                                                   ImageView imageView = (ImageView) v.findViewById(R.id.imageView2);
                                                   ParseFile file = mClickedClusterItem.getUserPhoto().getPhoto();
                                                   Bitmap bitmap = null;
                                                   try {
                                                       byte[] data = file.getData();
                                                       bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                   } catch (com.parse.ParseException e) {
                                                       Log.e(TAG, "error getting user photo bytes");
                                                       e.printStackTrace();
                                                   }
                                                   if (bitmap != null) {
                                                       imageView.setImageBitmap(bitmap);
                                                   }
                                               }
                                               else if (singleMark && !unlockFlag){
                                                   ImageView imageView = (ImageView) v.findViewById(R.id.imageView2);
                                                   imageView.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.locked));
                                               }
                                               else if (clusterMark){
                                                   ImageView imageView = (ImageView) v.findViewById(R.id.imageView2);
                                                   imageView.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.test));
                                                   TextView txtV = (TextView) v.findViewById(R.id.textView);
                                                   txtV.setText("Number of Photos:" + mClusterSize);
                                               }
                                               // Returning the view containing InfoWindow contents
                                               singleMark = false;
                                               clusterMark = false;
                                               unlockFlag = false;
                                               return v;

                                           }
                                       });


        // Enable the current location "blue dot".
        googleMap.setMyLocationEnabled(true);
        // Set up the camera change handler.
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                // When the camera changes, reconfigure the map.
                setUpClusterer();
            }
        });

        setUpClusterer();

        return rootView;
    }

    // Set up a customized query
    ParseQueryAdapter.QueryFactory<UserPhoto> factory =
            new ParseQueryAdapter.QueryFactory<UserPhoto>() {
                public ParseQuery<UserPhoto> create() {
                    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                    ParseQuery<UserPhoto> query = UserPhoto.getQuery();
                    query.include("user");
                    query.orderByDescending("createdAt");
                    query.whereWithinKilometers("location", geoPointFromLocation(myLoc), radius
                            * METERS_PER_FEET / METERS_PER_KILOMETER);
                    query.setLimit(MAX_POST_SEARCH_RESULTS);
                    return query;
                }
            };

    public Location getCurrentLocation() {
        return currentLocation;
    }

    /*
     * Called when the Activity is no longer visible at all. Stop updates and disconnect.
     */
    @Override
    public void onStop() {
        // If the client is connected
        if (locationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        locationClient.disconnect();

        super.onStop();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        locationClient = new LocationClient(TabActivity.c, this, this);

        // Connect to the location services client
        locationClient.connect();

    }

    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates((LocationListener) this);
    }

    private Location getLocation() {
        if (servicesConnected()) {
            Log.d(TAG, locationClient.getLastLocation().toString());
            return locationClient.getLastLocation();
        } else {
            return null;
        }
    }

    public void onConnected(Bundle bundle) {
        currentLocation = getLocation();
        startPeriodicUpdates();
    }

    public void onDisconnected() {
        Log.d(TAG, "disconnected from location services");
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
            Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorCode());
        }
    }

    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (lastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
            return;
        }
        lastLocation = location;

        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!hasSetUpInitialLocation) {
            // Zoom to the current location.
            updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        // Update map radius indicator
        updateCircle(myLatLng);
        setUpClusterer();
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Log.e(TAG, "could not get Google Play services");
            return false;
        }
    }

    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    /*
   * Displays a circle on the map representing the search radius
   */
    private void updateCircle(LatLng myLatLng) {
        if (mapCircle == null) {
            mapCircle =
                    mapFragment.getMap().addCircle(
                            new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
            int baseColor = Color.DKGRAY;
            mapCircle.setStrokeColor(baseColor);
            mapCircle.setStrokeWidth(2);
            mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor),
                    Color.blue(baseColor)));
        }
        mapCircle.setCenter(myLatLng);
        mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
    }

    /*
     * Zooms the map to show the area of interest based on the search radius
     */
    private void updateZoom(LatLng myLatLng) {
        // Get the bounds to zoom to
        LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);

       /*
        * Zoom to the given bounds (old bound method)
        * mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
        */

        // new method not in max zoom
        mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));

    }

    /*
     * Helper method to calculate the offset for the bounds used in map zooming
     */
    private double calculateLatLngOffset(LatLng myLatLng, boolean bLatOffset) {
        // The return offset, initialized to the default difference
        double latLngOffset = OFFSET_CALCULATION_INIT_DIFF;
        // Set up the desired offset distance in meters
        float desiredOffsetInMeters = radius * METERS_PER_FEET;
        // Variables for the distance calculation
        float[] distance = new float[1];
        boolean foundMax = false;
        double foundMinDiff = 0;
        // Loop through and get the offset
        do {
            // Calculate the distance between the point of interest
            // and the current offset in the latitude or longitude direction
            if (bLatOffset) {
                Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude
                        + latLngOffset, myLatLng.longitude, distance);
            } else {
                Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude,
                        myLatLng.longitude + latLngOffset, distance);
            }
            // Compare the current difference with the desired one
            float distanceDiff = distance[0] - desiredOffsetInMeters;
            if (distanceDiff < 0) {
                // Need to catch up to the desired distance
                if (!foundMax) {
                    foundMinDiff = latLngOffset;
                    // Increase the calculated offset
                    latLngOffset *= 2;
                } else {
                    double tmp = latLngOffset;
                    // Increase the calculated offset, at a slower pace
                    latLngOffset += (latLngOffset - foundMinDiff) / 2;
                    foundMinDiff = tmp;
                }
            } else {
                // Overshot the desired distance
                // Decrease the calculated offset
                latLngOffset -= (latLngOffset - foundMinDiff) / 2;
                foundMax = true;
            }
        } while (Math.abs(distance[0] - desiredOffsetInMeters) > OFFSET_CALCULATION_ACCURACY);
        return latLngOffset;
    }

    /*
     * Helper method to calculate the bounds for map zooming
     */
    LatLngBounds calculateBoundsWithCenter(LatLng myLatLng) {
        // Create a bounds
        LatLngBounds.Builder builder = LatLngBounds.builder();

        // Calculate east/west points that should to be included
        // in the bounds
        double lngDifference = calculateLatLngOffset(myLatLng, false);
        LatLng east = new LatLng(myLatLng.latitude, myLatLng.longitude + lngDifference);
        builder.include(east);
        LatLng west = new LatLng(myLatLng.latitude, myLatLng.longitude - lngDifference);
        builder.include(west);

        // Calculate north/south points that should to be included
        // in the bounds
        double latDifference = calculateLatLngOffset(myLatLng, true);
        LatLng north = new LatLng(myLatLng.latitude + latDifference, myLatLng.longitude);
        builder.include(north);
        LatLng south = new LatLng(myLatLng.latitude - latDifference, myLatLng.longitude);
        builder.include(south);

        return builder.build();
    }

    private class MyClusterRenderer extends DefaultClusterRenderer<SiteShotClusterItem> {
        public MyClusterRenderer() {
            super(getActivity(), mapFragment.getMap(), mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(SiteShotClusterItem item, MarkerOptions markerOptions) {
            double latitude = item.getPosition().latitude;
            double longitude = item.getPosition().longitude;
            ParseGeoPoint markerPoint = new ParseGeoPoint(latitude, longitude);

            Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
            final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);

            UserPhoto photo = item.getUserPhoto();
            ArrayList<String> unlocked = (ArrayList) photo.getList("unlocked");
            String username = ParseUser.getCurrentUser().getUsername();
            if (unlocked != null && unlocked.contains(username)) {
                    markerOptions.title("Unlocked")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (markerPoint.distanceInKilometersTo(myPoint) > radius * METERS_PER_FEET
                    / METERS_PER_KILOMETER) {
                // Display a gray marker with a predefined title and no snippet.
                markerOptions.title(getResources().getString(R.string.post_out_of_range)).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                // TODO: change cyan to gray after implementing custom marker icons
            } else {
                // Display a green marker with the post information.
                markerOptions.title("TODO: Image thumbnail")//.snippet(photo.getUser().getUsername())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

        }

        @Override
        protected void onBeforeClusterRendered(Cluster<SiteShotClusterItem> cluster, MarkerOptions markerOptions) {
            double latitude = cluster.getPosition().latitude;
            double longitude = cluster.getPosition().longitude;
            ParseGeoPoint markerPoint = new ParseGeoPoint(latitude, longitude);

            Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
            final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);

            if (markerPoint.distanceInKilometersTo(myPoint) > radius * METERS_PER_FEET
                    / METERS_PER_KILOMETER) {
                // Display a gray marker with a predefined title and no snippet.
                markerOptions.title(getResources().getString(R.string.post_out_of_range)).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                // TODO: change cyan to gray after implementing custom marker icons
            } else {
                // Display a green marker with the post information.
                markerOptions.title("TODO: Image thumbnail")//.snippet(photo.getUser().getUsername())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    private class ClusterListener implements
            ClusterManager.OnClusterInfoWindowClickListener<SiteShotClusterItem>,
            ClusterManager.OnClusterClickListener<SiteShotClusterItem>,
            ClusterManager.OnClusterItemClickListener<SiteShotClusterItem>,
            ClusterManager.OnClusterItemInfoWindowClickListener<SiteShotClusterItem> {

        @Override
        public boolean onClusterClick(Cluster<SiteShotClusterItem> cluster) {
            clusterMark = true;
            mClickedCluster = cluster;
            mClusterSize = cluster.getSize();


            Iterator itr;
            itr = cluster.getItems().iterator();
            while (itr.hasNext()) {
                Object element = itr.next();
                SiteShotClusterItem temp = (SiteShotClusterItem) element;
                UserPhoto clusterPhoot = temp.getUserPhoto();

                objectID = clusterPhoot.getObjectId();
                clusterContents.add(objectID);

            }
            unlockClusterIfNeeded(cluster);
            return false;
        }

        @Override
        public void onClusterInfoWindowClick(Cluster<SiteShotClusterItem> cluster) {
            Intent clusterViewIntent = new Intent(getActivity(), ClusterViewActivity.class);

            clusterViewIntent.putExtra("cluster", clusterContents);
            getActivity().startActivity(clusterViewIntent);
        }

        @Override
        public boolean onClusterItemClick(SiteShotClusterItem item) {
            singleMark = true;
            mClickedClusterItem = item;
            unlockItemIfNeeded(item);
            displayItem(item);

            // Center on the tapped marker and show its info window.
            return false;
        }

        private void displayItem(SiteShotClusterItem item) {
            UserPhoto phoot = item.getUserPhoto();
            String username = ParseUser.getCurrentUser().getUsername();
            ArrayList<String> unlocked = (ArrayList) phoot.getList("unlocked");

            if (unlocked.contains(username)) {
                unlockFlag = true;
            }
            else if (unlocked.contains(username)){
                unlockFlag = false;
            }
        }

        private void unlockClusterIfNeeded(Cluster<SiteShotClusterItem> cluster){

            Iterator itr;
            itr = cluster.getItems().iterator();
            while (itr.hasNext()){
                Object element = itr.next();
                SiteShotClusterItem temp = (SiteShotClusterItem) element;


                double latitude = temp.getPosition().latitude;
                double longitude = temp.getPosition().longitude;
                ParseGeoPoint markerPoint = new ParseGeoPoint(latitude, longitude);
                ParseGeoPoint myPoint = geoPointFromLocation(currentLocation);
                UserPhoto phoot = temp.getUserPhoto();
                String username = ParseUser.getCurrentUser().getUsername();
                ArrayList<String> unlocked = (ArrayList) phoot.getList("unlocked");

                // TODO: re-query UserPhoto in case it changed in the meantime.
                if (markerPoint.distanceInKilometersTo(myPoint) <= radius * METERS_PER_FEET
                        / METERS_PER_KILOMETER) {
                if (!unlocked.contains(username)) {
                    // Persist the unlocked state.
                    unlocked.add(username);
                    phoot.put("unlocked", unlocked);
                    phoot.saveInBackground();
                    cluster.getItems().iterator().next().setUserPhoto(phoot);

                    // Update it locally for setUpClusterer()
                    UserPhoto photoToRemove = null;
                    List<UserPhoto> photos = PhotoUtils.getInstance().getUserPhotos();
                    for (UserPhoto photo : photos) {
                        if (photo.getObjectId().equals(phoot.getObjectId())) {
                            photoToRemove = photo;
                        }
                    }
                    if (photoToRemove != null) {
                        photos.remove(photoToRemove);
                        photos.add(phoot);
                    }

                    unlockFlag = true;
                    // Re-draw the cluster items.
                    setUpClusterer();
                }
            }
            else {
                unlockFlag = false;
            }
            }
        }

        private void unlockItemIfNeeded(SiteShotClusterItem item) {
            double latitude = item.getPosition().latitude;
            double longitude = item.getPosition().longitude;
            ParseGeoPoint markerPoint = new ParseGeoPoint(latitude, longitude);
            ParseGeoPoint myPoint = geoPointFromLocation(currentLocation);

            // Unlock the marker if it's within range.
            if (markerPoint.distanceInKilometersTo(myPoint) <= radius * METERS_PER_FEET
                    / METERS_PER_KILOMETER) {

                UserPhoto phoot = item.getUserPhoto();
                String username = ParseUser.getCurrentUser().getUsername();
                ArrayList<String> unlocked = (ArrayList) phoot.getList("unlocked");

                // TODO: re-query UserPhoto in case it changed in the meantime.

                if (!unlocked.contains(username)) {
                    // Persist the unlocked state.
                    unlocked.add(username);
                    phoot.put("unlocked", unlocked);
                    phoot.saveInBackground();
                    item.setUserPhoto(phoot);

                    // Update it locally for setUpClusterer()
                    UserPhoto photoToRemove = null;
                    List<UserPhoto> photos = PhotoUtils.getInstance().getUserPhotos();
                    for (UserPhoto photo : photos) {
                        if (photo.getObjectId().equals(phoot.getObjectId())) {
                            photoToRemove = photo;
                        }
                    }
                    if (photoToRemove != null) {
                        photos.remove(photoToRemove);
                        photos.add(phoot);
                    }

                    unlockFlag = true;
                    // Re-draw the cluster items.
                    setUpClusterer();
                }
            }
            else {
                unlockFlag = false;
            }
        }

        @Override
        public void onClusterItemInfoWindowClick(SiteShotClusterItem item) {
            // Does nothing, but you could go into the user's profile page, for example.
        }

    }

    public void setUpClusterer() {
        // Declare a variable for the cluster manager.

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<SiteShotClusterItem>(getActivity(), mapFragment.getMap());
        mClusterManager.setRenderer(new MyClusterRenderer());

        // Set listeners.
        mClusterListener = new ClusterListener();
        mClusterManager.setOnClusterClickListener(mClusterListener);
        mClusterManager.setOnClusterInfoWindowClickListener(mClusterListener);
        mClusterManager.setOnClusterItemClickListener(mClusterListener);
        mClusterManager.setOnClusterItemInfoWindowClickListener(mClusterListener);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mapFragment.getMap().setOnCameraChangeListener(mClusterManager);
        mapFragment.getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {
        final int myUpdateNumber = ++mostRecentMapUpdate;
        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        // If location info isn't available, clean up any existing markers
        if (myLoc == null) {
            return;
        }

        List<UserPhoto> objects = PhotoUtils.getInstance().updateUserPhotos();

        if (myUpdateNumber != mostRecentMapUpdate) {
            return;
        }

        // Loop through the results of the search
        for (UserPhoto photo : objects) {
            SiteShotClusterItem offsetItem = new SiteShotClusterItem(
                    photo.getLocation().getLatitude(),
                    photo.getLocation().getLongitude(),
                    photo);

            mClusterManager.addItem(offsetItem);
        }
    }
}
