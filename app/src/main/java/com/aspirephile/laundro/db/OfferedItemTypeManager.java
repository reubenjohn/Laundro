package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.tables.OfferedItemType;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.COST;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.SERVICE;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.SQL_DELETE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.OfferedItemType.TABLE_NAME;

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

    public QueryStatement getOfferedItemTypeQuery(long serviceId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        "(select name from ItemType where ItemType._id = OfferedItemType._id)",
                        COST},
                SERVICE + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(serviceId)});
    }

    @NonNull
    public List<OfferedItemType> getOfferedItemTypeListFromCursor(@NonNull Cursor c) {
        ArrayList<OfferedItemType> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                OfferedItemType offeredItemType = new OfferedItemType();
                offeredItemType._id = c.getLong(0);
                offeredItemType.itemTypeName = c.getString(1);
                offeredItemType.cost = c.getFloat(2);
                list.add(offeredItemType);
            }
        } finally {
            c.close();
        }
        return list;
    }

}
