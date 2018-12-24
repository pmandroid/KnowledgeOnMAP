package com.vfig.konm.knowledgeonmap.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.fragment.MapFragment;
import com.vfig.konm.knowledgeonmap.parser.beans.WikiPageDetail;
import com.vfig.konm.knowledgeonmap.utils.NavigationUtils;
import com.vfig.konm.knowledgeonmap.utils.SharedPrefManager;

import java.util.List;

public class MapsMainActivity extends BaseActivity {


    public List<WikiPageDetail> wikiPageDetails;

    public SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        setupAdAtBottom();
        sharedPrefManager = SharedPrefManager.getInstance(MapsMainActivity.this);

        if (sharedPrefManager.loadPreferencesBoolean(FIRST_TIME)) {
            sharedPrefManager.savePreferences(FIRST_TIME, false);
            oneTimeDialog(MapsMainActivity.this, "Click on Map on any location to get" +
                    " Near by Places of Importance by Wikipedia." +
                    " \n" +
                    "Zoom Map and Click for better results!");
        } else {
            if (isNetworkAvailable(MapsMainActivity.this))
                addFragment(MapFragment.newInstance(), false);
            else
                networkDialog(MapsMainActivity.this);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_search:
                if (isNetworkAvailable(MapsMainActivity.this))
                    NavigationUtils.navigateToSearch(this);
                else
                    networkDialog(MapsMainActivity.this);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void oneTimeDialog(final Activity activity, String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style
                    .CustomDialogTheme);
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
                                        if (isNetworkAvailable(MapsMainActivity.this))
                                            addFragment(MapFragment.newInstance(), false);
                                        else
                                            networkDialog(MapsMainActivity.this);
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
}
