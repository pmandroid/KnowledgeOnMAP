package com.vfig.konm.knowledgeonmap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Constant {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String NAVIGATE_SEARCH = "navigate_search";
    public static final String URL_WIKI = "https://en.m.wikipedia.org/wiki/";

    public static Boolean getLocationPermission(Context context) {

        Boolean mLocationPermissionGranted = false;
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        return mLocationPermissionGranted;
    }
}
