package com.aspirephile.laundro.db;

import android.database.sqlite.SQLiteDatabase;

public class ItemTypeManager extends TableManager {
    ItemTypeManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LaundroContract.ItemType.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(SQLiteDatabase db) {
        db.execSQL(LaundroContract.ItemType.SQL_DELETE_ENTRIES);

    }

}
