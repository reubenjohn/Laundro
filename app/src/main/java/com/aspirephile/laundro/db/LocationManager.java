package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.tables.Location;

import static com.aspirephile.laundro.db.LaundroContract.Location.LAT;
import static com.aspirephile.laundro.db.LaundroContract.Location.LON;
import static com.aspirephile.laundro.db.LaundroContract.Location.NAME;
import static com.aspirephile.laundro.db.LaundroContract.Location.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Location.SQL_DELETE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Location.TABLE_NAME;
import static com.aspirephile.laundro.db.LaundroContract.Location._ID;

public class LocationManager extends TableManager {

    LocationManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public QueryStatement getLocation(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, NAME, LAT, LON},
                _ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(id)});
    }

    public Location getLocationFromCursor(Cursor c) {
        c.moveToFirst();
        Location location = new Location();
        location._id = c.getLong(0);
        location.name = c.getString(1);
        location.lon = c.getFloat(2);
        location.lat = c.getFloat(3);
        return location;
    }

    public UpdateStatement updateName(String email, String name) {
        ContentValues values = new ContentValues();
        values.put(LAT, name);
        return new UpdateStatement(dbHelper,
                TABLE_NAME,
                values,
                NAME + "=?",
                new String[]{email});
    }
}
