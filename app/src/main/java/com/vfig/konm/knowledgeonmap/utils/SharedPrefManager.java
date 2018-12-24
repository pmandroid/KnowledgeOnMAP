package com.vfig.konm.knowledgeonmap.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPreferences prefs;
    private static SharedPrefManager _instance;
    private static final String prefName ="KNOP_PREF";

    private SharedPrefManager() {

    }

    public static SharedPrefManager getInstance(Context aContext) {
        if (aContext != null) {
            prefs = aContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            if (_instance == null) {
                _instance = new SharedPrefManager();
            }
        }
        return _instance;
    }


    public  void savePreferences(String key, String value) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public  String loadPreferences(String key) {

        return prefs.getString(key, "");
    }

    public  int loadPreferencesInt(String key) {

        return prefs.getInt(key, Integer.MIN_VALUE);
    }

    public  void savePreferences(String key, int value) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public  boolean loadPreferencesBoolean(String key) {

        return prefs.getBoolean(key, true);
    }


    public  void savePreferences(String key, boolean value) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public  boolean isContains(String key) {
        return prefs.contains(key);
    }

    public  void removePreferences(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

}
