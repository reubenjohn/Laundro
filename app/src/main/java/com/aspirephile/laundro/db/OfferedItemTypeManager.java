package com.aspirephile.laundro.db;

import android.database.sqlite.SQLiteDatabase;

import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.SQL_DELETE_ENTRIES;

public class OfferedItemTypeManager extends TableManager {
    OfferedItemTypeManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);

    }

}
