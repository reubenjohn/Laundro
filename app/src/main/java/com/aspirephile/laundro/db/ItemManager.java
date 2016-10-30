package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.tables.Service;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.Item.*;

public class ItemManager extends TableManager {
    ItemManager(LaundroDb dbHelper) {
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

    public QueryStatement getOfferedItemTypes() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, OFFERED_ITEM_TYPE, BILL},
                null,
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, null);
    }

    @NonNull
    public List<Service> getOfferedItemTypesFromResult(@NonNull Cursor c) {
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

    public QueryStatement getOfferedItemTypes(int _id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, OFFERED_ITEM_TYPE, BILL},
                LaundroContract.Service._ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(_id)});
    }

    public Service getOfferedItemTypeFromResult(@NonNull Cursor c) {
        List<Service> services = getOfferedItemTypesFromResult(c);
        if (services.size() > 0)
            return services.get(0);
        else
            return null;
    }
}
