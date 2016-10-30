package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.LaundroContract.Bill;
import com.aspirephile.laundro.db.tables.User;

public class BillManager extends TableManager {

    BillManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(Bill.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(Bill.SQL_DELETE_ENTRIES);
    }

    public QueryStatement getBill(int id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Bill.TABLE_NAME);
        String query = qb.buildQuery(new String[]{Bill.USER, Bill.ISSUED_AT, Bill.PAYED_AT},
                Bill._ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(id)});
    }

    public User getBillFromResult(Cursor c) {
        c.moveToFirst();
        return new User(c);
    }

    public UpdateStatement payNow(int id) {
        ContentValues values = new ContentValues();
        values.put(Bill.PAYED_AT, System.currentTimeMillis());
        return new UpdateStatement(dbHelper,
                Bill.TABLE_NAME,
                values,
                Bill._ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
