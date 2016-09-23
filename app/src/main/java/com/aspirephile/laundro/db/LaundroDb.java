package com.aspirephile.laundro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.LaundroContract.User;

/**
 * Created by Reuben John on 9/9/2016.
 */
public class LaundroDb extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Laundro.db";
    private static LaundroDb dbHelper = null;

    private static UserManager userManager;

    public LaundroDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(User.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void initialize(@NonNull Context applicationContext) {
        dbHelper = new LaundroDb(applicationContext);
    }

    @NonNull
    public static UserManager getUserManager() {
        if (userManager != null)
            return userManager;
        else
            return (userManager = new UserManager(dbHelper));
    }
}