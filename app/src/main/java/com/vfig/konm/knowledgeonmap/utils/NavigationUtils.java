package com.vfig.konm.knowledgeonmap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.common.internal.Constants;
import com.vfig.konm.knowledgeonmap.activity.SearchActivity;

public class NavigationUtils {

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(Constant.NAVIGATE_SEARCH);
        context.startActivity(intent);
    }

    public static void openWebPage(Context context,String title) {
        Uri uri = Uri.parse(Constant.URL_WIKI + title);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
