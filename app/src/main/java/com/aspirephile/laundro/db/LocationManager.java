package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.LaundroContract.Location;
import com.aspirephile.laundro.db.tables.User;

public class LocationManager extends TableManager {

    LocationManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(Location.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(Location.SQL_DELETE_ENTRIES);
    }

    public QueryStatement getLocation(@NonNull String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Location.TABLE_NAME);
        String query = qb.buildQuery(new String[]{Location.LAT, Location.LON},
                Location.NAME + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{name});
    }

    public User getUserFromResult(Cursor c) {
        c.moveToFirst();
        return new User(c);
    }

    public UpdateStatement updateName(String email, String name) {
        ContentValues values = new ContentValues();
        values.put(Location.LAT, name);
        return new UpdateStatement(dbHelper,
                Location.TABLE_NAME,
                values,
                Location.NAME + "=?",
                new String[]{email});
    }
}
