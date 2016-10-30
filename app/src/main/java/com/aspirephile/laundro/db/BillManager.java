package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.tables.Bill;
import com.aspirephile.laundro.db.tables.Service;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.Bill.ISSUED_AT;
import static com.aspirephile.laundro.db.LaundroContract.Bill.PAYED_AT;
import static com.aspirephile.laundro.db.LaundroContract.Bill.SERVICE;
import static com.aspirephile.laundro.db.LaundroContract.Bill.TABLE_NAME;

public class BillManager extends TableManager {

    BillManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(LaundroContract.Bill.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(LaundroContract.Bill.SQL_DELETE_ENTRIES);
    }


    public QueryStatement getBillListQuery() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        "(select " + LaundroContract.Service.NAME + " from " + LaundroContract.Service.TABLE_NAME + " where " + LaundroContract.Service.TABLE_NAME + "." + LaundroContract.Service._ID + " = " + TABLE_NAME + "." + SERVICE + ")",
                        ISSUED_AT,
                        PAYED_AT},
                null,
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, null);
    }

    @NonNull
    public List<Bill> getBillListFromResult(@NonNull Cursor c) {
        ArrayList<Bill> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                Bill bill = new Bill();
                bill._id = c.getInt(0);
                bill.service = new Service();
                bill.service.name = c.getString(1);
                bill.issuedAt = c.getLong(2);
                bill.payedAt = c.getLong(3);
                list.add(bill);
            }
        } finally {
            c.close();
        }
        return list;
    }

    public UpdateStatement payNow(int id) {
        ContentValues values = new ContentValues();
        values.put(LaundroContract.Bill.PAYED_AT, System.currentTimeMillis());
        return new UpdateStatement(dbHelper,
                LaundroContract.Bill.TABLE_NAME,
                values,
                LaundroContract.Bill._ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public QueryStatement getBill(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        SERVICE,
                        "(select " + LaundroContract.Service.NAME + " from " + LaundroContract.Service.TABLE_NAME + " where " + LaundroContract.Service.TABLE_NAME + "." + LaundroContract.Service._ID + " = " + TABLE_NAME + "." + SERVICE + ")",
                        ISSUED_AT,
                        PAYED_AT},
                _ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(id)});
    }

    public Bill getBillFromResult(Cursor c) {
        c.moveToFirst();
        Bill bill = new Bill();
        bill._id = c.getInt(0);
        bill.service = new Service();
        bill.service._id = c.getLong(1);
        bill.service.name = c.getString(2);
        bill.issuedAt = c.getLong(2);
        bill.payedAt = c.getLong(3);
        return bill;
    }
}
