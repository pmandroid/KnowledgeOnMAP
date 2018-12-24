package com.vfig.konm.knowledgeonmap.utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;


public class GPSTracker extends Service implements LocationListener {

    private static final String TAG = "GPSTracker";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static LocationManager manager;

    private static LocationRequest locationRequest;
    private static GPSTracker gpsTracker;
    private static Context mContext;
    /**
     * The Location manager.
     */
// Declaring a Location Manager
    protected LocationManager locationManager;
    /**
     * The Is gps enabled.
     */
// flag for GPS status
    boolean isGPSEnabled = false;
    /**
     * The Is network enabled.
     */
// flag for network status
    boolean isNetworkEnabled = false;
    /**
     * The Can get location.
     */
// flag for GPS status
    boolean canGetLocation = false;
    /**
     * The Location.
     */
    Location location; // location
    /**
     * The Latitude.
     */
    double latitude; // latitude
    /**
     * The Longitude.
     */
    double longitude; // longitude
    private Location mLocation;

    /**
     * Instantiates a new Gps tracker.
     *
     * @param context the context
     */
    private GPSTracker(Context context) {

        getLocation();
    }

    /**
     * Gets google api client instance.
     *
     * @param context the context
     * @return the google api client instance
     */
    public static GPSTracker getGoogleApiClientInstance(Context context) {
        mContext = context;
        manager = (LocationManager)mContext
                .getSystemService(LOCATION_SERVICE);

        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS && gpsTracker != null) {
            gpsTracker = null;

            locationRequest = null;
        }
        if (gpsTracker == null)
            gpsTracker = new GPSTracker(mContext);
        return gpsTracker;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
//    private GoogleApiClient getLocation() {
//        try {
//            SLog.e(TAG, "googleApiClient  created  " + googleApiClient);
//            if (googleApiClient == null) {
//                googleApiClient = new GoogleApiClient.Builder(GlobalAccess.getInstance()
//                        .getApplicationContext())
//                        .addApi(LocationServices.API).addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this).build();
//
//                locationRequest = LocationRequest.create();
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                locationRequest.setInterval(1000);
//                locationRequest.setFastestInterval(500);
//                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                        .addLocationRequest(locationRequest);
////                connect();
//                // **************************
//                builder.setAlwaysShow(true); // this is the key ingredient
//                // **************************
//
//                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
//                        .checkLocationSettings(googleApiClient, builder.build());
//                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                    @Override
//                    public void onResult(LocationSettingsResult result) {
//                        SLog.e(TAG, "LocationSettingsResult" + result);
//                        final Status status = result.getStatus();
//                        final LocationSettingsStates state = result
//                                .getLocationSettingsStates();
//                        switch (status.getStatusCode()) {
//                            case LocationSettingsStatusCodes.SUCCESS:
//                                // All location settings are satisfied. The client can
//                                // initialize location
//                                // requests here.
//                                break;
//                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                // Location settings are not satisfied. But could be
//                                // fixed by showing the user
//                                // a dialog.
//                                // Show the dialog by calling
//                                // startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                SLog.e(TAG, "get lattitude" + mContext);
//                                try {
//                                    status.startResolutionForResult((Activity) mContext, 1000);
//                                } catch (IntentSender.SendIntentException e) {
//                                    e.printStackTrace();
//                                }
//                                // Ignore the error.
//                                break;
//                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                // Location settings are not satisfied. However, we have
//                                // no way to fix the
//                                // settings so we won't show the dialog.
//                                break;
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return googleApiClient;
//    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission
                                    .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }else {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSTracker.this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission
                                    .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }else {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSTracker.this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    /**
     * Function to get latitude
     *
     * @return the double
     */
    public double getLatitude() {
        if (mLocation != null)
            latitude = mLocation.getLatitude();
        return latitude;
    }

    /**
     * Function to get longitude
     *
     * @return the double
     */
    public double getLongitude() {
        if (mLocation != null)
            longitude = mLocation.getLongitude();
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean boolean
     */
    public boolean canGetLocation() {
        return true;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//
//        /*// Setting Dialog Title
//        alertDialog.setTitle("GPS is settings");
//  */
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is disabled in your device. Would you like to enable it?");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("GPS Setting", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                mContext.startActivity(intent);
//            }
//        });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        SLog.e(TAG, "Location tracker connected" + mLocation);
//        if (mLocation == null) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
//                    locationRequest, this);
//        } else {
//            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        SLog.e(TAG, "Location tracker suspended");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        SLog.e(TAG, "Location tracker failed");
//    }

    /**
     * Connect.
     */
    public void connect() {
//        SLog.e(TAG, "connected" + googleApiClient.isConnected());
//        if (!googleApiClient.isConnected())
//            googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocation == null && location != null) {
            mLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}