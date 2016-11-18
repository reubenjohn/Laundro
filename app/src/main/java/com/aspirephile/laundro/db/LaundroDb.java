package com.aspirephile.laundro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * Created by Reuben John on 9/9/2016.
 */
public class LaundroDb extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Laundro.db";
    private static LaundroDb dbHelper = null;

    private static UserManager userManager;
    private static ServiceManager serviceManager;
    private static LocationManager locationManager;
    private static BillManager billManager;
    private static ItemTypeManager itemTypeManagerManager;
    private static OfferedItemTypeManager offeredItemTypeManagerManager;
    private static ItemManager itemManager;
    private static ReviewManager reviewManager;

    public LaundroDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initialize(@NonNull Context applicationContext) {
        dbHelper = new LaundroDb(applicationContext);
    }

    @NonNull
    public static LocationManager getLocationManager() {
        if (locationManager != null)
            return locationManager;
        else
            return (locationManager = new LocationManager(dbHelper));
    }

    @NonNull
    public static UserManager getUserManager() {
        if (userManager != null)
            return userManager;
        else
            return (userManager = new UserManager(dbHelper));
    }

    @NonNull
    public static ServiceManager getServiceManager() {
        if (serviceManager != null)
            return serviceManager;
        else
            return (serviceManager = new ServiceManager(dbHelper));
    }

    @NonNull
    public static BillManager getBillManager() {
        if (billManager != null)
            return billManager;
        else
            return (billManager = new BillManager(dbHelper));
    }

    @NonNull
    public static ItemTypeManager getItemTypeManagerManager() {
        if (itemTypeManagerManager != null)
            return itemTypeManagerManager;
        else
            return (itemTypeManagerManager = new ItemTypeManager(dbHelper));
    }

    @NonNull
    public static OfferedItemTypeManager getOfferedItemTypeManagerManager() {
        if (offeredItemTypeManagerManager != null)
            return offeredItemTypeManagerManager;
        else
            return (offeredItemTypeManagerManager = new OfferedItemTypeManager(dbHelper));
    }

    @NonNull
    public static ItemManager getItemManager() {
        if (itemManager != null)
            return itemManager;
        else
            return (itemManager = new ItemManager(dbHelper));
    }

    @NonNull
    public static ReviewManager getReviewManager() {
        if (reviewManager != null)
            return reviewManager;
        else
            return (reviewManager = new ReviewManager(dbHelper));
    }

    public void onCreate(SQLiteDatabase db) {
        getLocationManager().onCreate(db);
        getUserManager().onCreate(db);
        getServiceManager().onCreate(db);
        getBillManager().onCreate(db);
        getItemTypeManagerManager().onCreate(db);
        getOfferedItemTypeManagerManager().onCreate(db);
        getItemManager().onCreate(db);
        getReviewManager().onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        getLocationManager().onDestroy(db);
        getServiceManager().onDestroy(db);
        getUserManager().onDestroy(db);
        getBillManager().onDestroy(db);
        getItemTypeManagerManager().onDestroy(db);
        getOfferedItemTypeManagerManager().onDestroy(db);
        getItemManager().onDestroy(db);
        getReviewManager().onDestroy(db);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}