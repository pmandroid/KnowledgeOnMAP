package com.vfig.konm.knowledgeonmap.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.activity.MapsMainActivity;
import com.vfig.konm.knowledgeonmap.network.AsyncController;
import com.vfig.konm.knowledgeonmap.network.WebServiceHandlerInterface;
import com.vfig.konm.knowledgeonmap.parser.beans.WikiPageDetail;
import com.vfig.konm.knowledgeonmap.utils.GPSTracker;
import com.vfig.konm.knowledgeonmap.utils.ICancelProgress;
import com.vfig.konm.knowledgeonmap.utils.IRequestType;
import com.vfig.konm.knowledgeonmap.utils.ProgressDialogue;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.vfig.konm.knowledgeonmap.activity.BaseActivity.FIRST_TIME;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        WebServiceHandlerInterface, ICancelProgress {
    private static final String TAG = "LOG";
    private static final int DEFAULT_ZOOM = 14;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    double lat = 51.508530;
    double lng = -0.076132;
    private final LatLng mDefaultLocation = new LatLng(lat, lng);
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng latlng;
    private AsyncTask<String, Integer, List<WikiPageDetail>> asyncTask;

    public MapFragment() {

    }


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);

        }
        try {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                    (getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inflateView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        ProgressDialogue.setCancelProgress(MapFragment.this);

        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocationPermission();

        AsyncController.setWebServiceHandlerInterface(MapFragment.this);
        return inflateView;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                if (((MapsMainActivity) getActivity()).isNetworkAvailable(getActivity())) {

                    if (mLocationPermissionGranted) {
                        latlng = point;

                        lat = latlng.latitude;
                        lng = latlng.longitude;

                        DecimalFormat decimalFormat = new DecimalFormat("#0.00000");

                        String latitude = "" + lat;
                        String longitude = "" + lng;
                        try {
                            latitude = decimalFormat.format(lat);
                            longitude = decimalFormat.format(lng);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latlng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title("LOCATION");
                        markerOptions.snippet("Latitude : " + latitude + "\n" +
                                "Longitude : " +
                                longitude);

                        // Clears the previously touched position
                        mMap.clear();


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);


                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(getActivity());
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getActivity());
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getActivity());
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

//                    api.php?action=query&prop=coordinates|pageimages|pageterms&colimit=50
// &piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description&generator=geosearch
// &ggscoord=37.786952|-122.399523&ggsradius=10000&ggslimit=50


                        ProgressDialogue.showProgressDialog(getActivity());

                        final String AMPERSAND = "&";
                        final String LINE = "|";
                        final String LIMIT = "50";
                        final String RADIUS = "10000";
                        final String LATITUDE = String.valueOf(lat);
                        final String LONGITUDE = String.valueOf(lng);
                        final String THUMB_SIZE = "144";
                        final String BASE_API = "https://en.wikipedia.org/w/api";
                        String url = BASE_API +
                                ".php?action=" +
                                "query" + AMPERSAND +
                                "prop=" +
                                "coordinates"
                                + LINE +
                                "pageimages" +
                                LINE +
                                "pageterms" +
                                AMPERSAND +
                                "colimit" + "=" +
                                LIMIT +
                                AMPERSAND +
                                "piprop=" +
                                "thumbnail" +
                                AMPERSAND +
                                "pithumbsize=" +
                                THUMB_SIZE +
                                AMPERSAND +
                                "pilimit=" +
                                LIMIT +
                                AMPERSAND +
                                "wbptterms=" +
                                "description" +
                                AMPERSAND +
                                "generator=" +
                                "geosearch" +
                                AMPERSAND +
                                "ggscoord=" +
                                LATITUDE +
                                LINE +
                                LONGITUDE +
                                AMPERSAND +
                                "ggsradius=" +
                                RADIUS +
                                AMPERSAND +
                                "ggslimit=" +
                                LIMIT +
                                AMPERSAND +
                                "format=" +
                                "json";

                        //    Log.d(TAG, "onMapClick: URL = " + url);

                        HashMap<String, String> hashMap = new HashMap<>();
                        asyncTask = new
                                AsyncController<List<WikiPageDetail>>(getActivity(), url, hashMap,
                                IRequestType._MAP)
                                .execute();

                    } else {
                        requestPermissions(
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                } else {
                    ((MapsMainActivity) getActivity()).networkDialog(getActivity());
                }


            }
        });
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

            final LocationManager manager = (LocationManager) getActivity()
                    .getSystemService(Context
                            .LOCATION_SERVICE);
            assert manager != null;
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();

                ((MapsMainActivity) getActivity()).sharedPrefManager.savePreferences(FIRST_TIME,
                        false);
            } else {
                updateLocationUI();
                getDeviceLocation();
            }


        } else {
            ((MapsMainActivity) getActivity()).sharedPrefManager.savePreferences(FIRST_TIME, false);
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                if (mFusedLocationProviderClient != null) {
                    if (mFusedLocationProviderClient.getLastLocation() != null) {

                        Task<Location> locationResult = mFusedLocationProviderClient
                                .getLastLocation();

                        locationResult.addOnCompleteListener(getActivity(), new
                                OnCompleteListener<Location>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Location> task) {
                                        if (task.isSuccessful()) {
                                            // Set the map's camera position to the current
                                            // location of
                                            // the device.
                                            mLastKnownLocation = task.getResult();
                                            if (mLastKnownLocation != null) {
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                                mLastKnownLocation.getLongitude()),
                                                        DEFAULT_ZOOM));
                                                mMap.setMyLocationEnabled(true);
                                                mMap.getUiSettings().setMyLocationButtonEnabled
                                                        (true);
                                            }

                                        } else {
                                            //    Log.d(TAG, "Current location is null. Using
                                            // defaults.");
                                            //    Log.e(TAG, "Exception: %s", task.getException());
                                            mMap.moveCamera(CameraUpdateFactory
                                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        }
                                    }
                                });
                    } else {
                        GPSTracker gps = GPSTracker.getGoogleApiClientInstance(getActivity());
                        gps.connect();
                        if (gps.canGetLocation() && gps.getLatitude() != 0.0) {
                            try {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(gps.getLatitude(),
                                                gps.getLongitude()),
                                        DEFAULT_ZOOM));
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled
                                        (true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                } else {
                    mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);


                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e) {
            //   Log.e("Exception: %s", e.getMessage());
        }
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder
                (getActivity(), R.style.CustomDialogTheme);
        alertDialog.setMessage("GPS is disabled in your device. Would you like to enable it?");
        alertDialog.setPositiveButton("GPS Setting", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onSuccess(Object object) {
        ProgressDialogue.hideProgressDialog();
        if (object != null) {
            ((MapsMainActivity) getActivity()).wikiPageDetails = (List<WikiPageDetail>) object;

            if (!((MapsMainActivity) getActivity()).wikiPageDetails.isEmpty()) {
                ((MapsMainActivity) getActivity()).addFragment(WikiMainFragment.newInstance(),
                        true);
            } else {
                okDialog("Alert!", "Sorry! No Data Available for this Location.Please Try " +
                        "Again...");

            }
        } else {
            okDialog("Alert!", "Sorry! No Data Available for this Location.Please Try Again...");
        }
    }

    @Override
    public void onError(Object o) {
        ProgressDialogue.hideProgressDialog();
        okDialog("Alert!", "Sorry! No Data Available for this Location.Please Try Again...");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;

                    final LocationManager manager = (LocationManager) getActivity()
                            .getSystemService(Context
                                    .LOCATION_SERVICE);
                    assert manager != null;
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showSettingsAlert();
                    } else {
                        updateLocationUI();
                        getDeviceLocation();
                    }
                } else if (shouldShowRequestPermissionRationale(Manifest.permission
                        .ACCESS_FINE_LOCATION)) {
                    //Show Information about why you need the permission
                    permissionDialog();
                } else {
                    okDialog("Alert!", "This app Requires Location permission");
                }
            }
        }
    }

    private void okDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style
                .CustomDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style
                .CustomDialogTheme);
        builder.setTitle("Need Location Permission");
        builder.setMessage("This app needs location permission");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();


                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (((MapsMainActivity) getActivity()).sharedPrefManager.loadPreferencesBoolean
                    (FIRST_TIME)) {
                getDeviceLocation();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateLocationUI();
    }

    @Override
    public void onDetach() {

        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTask.cancel(true);
        }
        super.onDetach();

    }

    @Override
    public void progressCanceled() {

        //    Log.e("cancel called","1");
//        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
//            asyncTask.cancel(true);
//        }
    }
}



