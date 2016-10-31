package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.async.QueryStatement;
import com.aspirephile.laundro.db.tables.Location;
import com.aspirephile.laundro.db.tables.Service;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.Service.CREATED_AT;
import static com.aspirephile.laundro.db.LaundroContract.Service.DESCRIPTION;
import static com.aspirephile.laundro.db.LaundroContract.Service.LOCATION;
import static com.aspirephile.laundro.db.LaundroContract.Service.NAME;
import static com.aspirephile.laundro.db.LaundroContract.Service.PHONE;
import static com.aspirephile.laundro.db.LaundroContract.Service.TABLE_NAME;

public class ServiceManager extends TableManager {
    ServiceManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LaundroContract.Service.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(SQLiteDatabase db) {
        db.execSQL(LaundroContract.Service.SQL_DELETE_ENTRIES);

    }

    public QueryStatement getServicesListQuery() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        NAME,
                        CREATED_AT,
                        LOCATION,
                        "(select " + LaundroContract.Location.NAME + " from " + LaundroContract.Location.TABLE_NAME + " where " + LaundroContract.Location.TABLE_NAME + "." + LaundroContract.Location._ID + " = " + TABLE_NAME + "." + LOCATION + ")",
                        PHONE,
                        DESCRIPTION},
                null,
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, null);
    }

    @NonNull
    public List<Service> getServiceListFromResult(@NonNull Cursor c) {
        ArrayList<Service> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                Service service = new Service();
                service._id = c.getInt(0);
                service.name = c.getString(1);
                service.createdAt = c.getLong(2);
                service.location = new Location();
                service.location._id = c.getLong(3);
                service.location.name = c.getString(4);
                service.phone = c.getString(5);
                service.description = c.getString(6);
                list.add(service);
            }
        } finally {
            c.close();
        }
        return list;
    }

    public QueryStatement getService(long _id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        NAME,
                        CREATED_AT,
                        LOCATION,
                        "(select " + LaundroContract.Location.NAME + " from " + LaundroContract.Location.TABLE_NAME + " where " + LaundroContract.Location.TABLE_NAME + "." + LaundroContract.Location._ID + " = " + TABLE_NAME + "." + LOCATION + ")",
                        PHONE,
                        DESCRIPTION},
                LaundroContract.Service._ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(_id)});
    }

    public Service getServiceFromResult(@NonNull Cursor c) {
        List<Service> services = getServiceListFromResult(c);
        if (services.size() > 0)
            return services.get(0);
        else
            return null;
    }
}
