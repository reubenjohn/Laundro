package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

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

    public QueryStatement getAllServices() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, NAME, CREATED_AT, LOCATION, PHONE, DESCRIPTION},
                null,
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, null);
    }

    @NonNull
    public List<Service> getServicesFromResult(@NonNull Cursor c) {
        ArrayList<Service> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                Service service = new Service(c);
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
        String query = qb.buildQuery(new String[]{_ID, NAME, CREATED_AT, LOCATION, PHONE, DESCRIPTION},
                LaundroContract.Service._ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(_id)});
    }

    public Service getServiceFromResult(@NonNull Cursor c) {
        List<Service> services = getServicesFromResult(c);
        if (services.size() > 0)
            return services.get(0);
        else
            return null;
    }
}
