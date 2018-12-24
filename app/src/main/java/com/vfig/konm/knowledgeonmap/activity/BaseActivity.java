package com.vfig.konm.knowledgeonmap.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vfig.konm.knowledgeonmap.R;


public class BaseActivity extends AdMobActivity {
    public static final String FIRST_TIME = "FIRST_TIME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable(BaseActivity.this)) {
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network " +
//                    "Unavailable!", Snackbar.LENGTH_SHORT);
//            snackbar.show();
        }


    }

    public boolean isNetworkAvailable(Activity activity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            if (netInfo != null) {
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected())
                            haveConnectedWifi = true;
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected())
                            haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void messageDialog(final Activity activity, String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialogTheme);
            String labelTextOK = "Ok";
            String labelTextTitle = "Message";
            String labelTextMessage = message;


            builder.setTitle(labelTextTitle);

            builder.setMessage(labelTextMessage)
                    .setCancelable(false)
                    .setPositiveButton(labelTextOK,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    try {
                                        dialog.cancel();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

            AlertDialog alert = builder.create();
            if (!alert.isShowing())
                alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void networkDialog(final Activity activity) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialogTheme);
            String labelTextOK = "Ok";
            String labelTextTitle = "Network Error";
            String labelTextMessage = "Seems like there is a problem with your internet " +
                    "connection. Check your network and try again.";


            builder.setTitle(labelTextTitle);

            builder.setMessage(labelTextMessage)
                    .setCancelable(false)
                    .setPositiveButton(labelTextOK,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    try {
                                        dialog.cancel();
                                        activity.finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

            AlertDialog alert = builder.create();
            if (!alert.isShowing())
                alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.getTag());
        fragmentTransaction.add(R.id.screen_container, fragment);
        fragmentTransaction.commit();
    }


}
