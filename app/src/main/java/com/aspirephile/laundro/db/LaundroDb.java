package com.aspirephile.laundro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

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

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        String[] insertStatements = {
                "insert into Location (name,lat,lon) values (\"MIT Hostels Block 14, Manipal, Karnataka\", 13.345326, 74.795502);",
                "insert into Location (name,lat,lon) values (\"MIT Hostels Block 16, Manipal, Karnataka\", 13.346004, 74.796683);",
                "insert into Location (name,lat,lon) values (\"Kediyoor Confectionaries, Brahmagiri, Udupi, Karnataka 576101, India\", 13.343019, 74.746565);",

                "insert into User (email,name) values (\"reubenvjohn@gmail.com\",\"Reuben John\");",
                "insert into Service (createdAt,name,location,phone) values (1474699503374,\"Snow White\", 1,7022131132);",
                "insert into Service (createdAt,name,location,phone) values (1474699403374,\"Pro Laundro\", 2, 7022131325);",
                "insert into Service (createdAt,name,location,phone) values (1474699203374,\"Ultima\", 3, 7022131603);",
                "insert into ItemType (name) values (\"T-Shirt\");",
                "insert into ItemType (name) values (\"Shorts\");",
                "insert into ItemType (name) values (\"Shirt\");",
                "insert into ItemType (name) values (\"Pants\");",

                "insert into OfferedItemType (itemType,service,cost) values (1,1,25);",
                "insert into OfferedItemType (itemType,service,cost) values (2,1,20);",
                "insert into OfferedItemType (itemType,service,cost) values (3,1,35);",
                "insert into OfferedItemType (itemType,service,cost) values (4,1,30);",

                "insert into Bill (user,service,issuedAt,payedAt) values (1,1, 1477827435162,-2);",
                "insert into Bill (user,service,issuedAt,payedAt) values (1,3, 1474699903374,-1);",
                "insert into Bill (user,service,issuedAt,payedAt) values (1,3, 1474699904411, 1477856024935);",

                "insert into Item (offeredItemType,bill) values (1,1);",
                "insert into Item (offeredItemType,bill) values (1,1);",
                "insert into Item (offeredItemType,bill) values (1,1);",
                "insert into Item (offeredItemType,bill) values (2,1); ",
                "insert into Item (offeredItemType,bill) values (2,1); ",
                "insert into Item (offeredItemType,bill) values (3,1); ",
                "insert into Item (offeredItemType,bill) values (4,1);",

                "insert into Review(service,user,timestamp,rating,description) values(1,1,1474699903374,4.5,\"Got my clothes back better than I bought it!\");"

        };
        for (String sql :
                insertStatements) {
            Log.d("SampleDataInsertion", sql);

            db.execSQL(sql);
        }
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