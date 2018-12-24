package com.vfig.konm.knowledgeonmap.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vfig.konm.knowledgeonmap.R;

public class AdMobActivity extends AppCompatActivity {
    FrameLayout content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NewApi")
    protected void setupAdAtBottom() {
        content = (FrameLayout) findViewById(android.R.id.content);
        // inflate ad layout and set it to bottom by layouparams
        final LinearLayout ad = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.add_mob_banner, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        ad.setLayoutParams(params);

        // adding viewtreeobserver to get height of ad layout , so that
        // android.R.id.content will set margin of that height
        ViewTreeObserver vto = ad.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    ad.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ad.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = ad.getMeasuredWidth();
                int height = ad.getMeasuredHeight();
            //    Log.i("ad hight", height + "");
                setSpaceForAd(height);

            }

        });
        addLayoutToContent(ad);

    }

    private void setSpaceForAd(int height) {

        // content.getChildAt(0).setPadding(0, 0, 0, 50);
        View child0 = content.getChildAt(0);
        FrameLayout.LayoutParams layoutparams = (android.widget.FrameLayout.LayoutParams) child0
                .getLayoutParams();
        layoutparams.bottomMargin = height;
        child0.setLayoutParams(layoutparams);

    }

    private void addLayoutToContent(View ad) {
        content.addView(ad);
        AdView mAdView = (AdView) ad.findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
