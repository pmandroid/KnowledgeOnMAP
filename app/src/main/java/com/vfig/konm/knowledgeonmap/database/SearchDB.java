//package com.vfig.konm.knowledgeonmap.database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class SearchDB extends SQLiteOpenHelper  {
//    public static final String DATABASENAME = "musicdb.db";
//    private static final int VERSION = 4;
//    private static SearchDB sInstance = null;
//
//    private final Context mContext;
//
//    public SearchDB(final Context context) {
//        super(context, DATABASENAME, null, VERSION);
//
//        mContext = context;
//    }
//
//    public static final synchronized SearchDB getInstance(final Context context) {
//        if (sInstance == null) {
//            sInstance = new SearchDB(context.getApplicationContext());
//        }
//        return sInstance;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        SearchHistory.getInstance(mContext).onCreate(db);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        SearchHistory.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
//    }
//
//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        SearchHistory.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
//    }
//}
