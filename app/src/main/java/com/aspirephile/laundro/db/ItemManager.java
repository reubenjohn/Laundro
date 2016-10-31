package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.async.QueryStatement;
import com.aspirephile.laundro.db.tables.Item;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.Item.BILL;
import static com.aspirephile.laundro.db.LaundroContract.Item.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Item.SQL_DELETE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Item.TABLE_NAME;

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

    public QueryStatement getItemQuery(long billId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{
                        _ID,
                        "(select name from ItemType where ItemType._id = (select itemType from OfferedItemType where OfferedItemType._id = Item.offeredItemType)) as name",
                        "(select cost from OfferedItemType where OfferedItemType._id = Item.offeredItemType) as cost",
                        "count(offeredItemType) as count"
                },
                BILL + "=?",
                "name",
                null,
                "count(offeredItemType) desc",
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(billId)});
    }

    @NonNull
    public List<Item> getItemListFromCursor(@NonNull Cursor c) {
        ArrayList<Item> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                Item item = new Item();
                item._id = c.getLong(0);
                item.itemTypeName = c.getString(1);
                item.cost = c.getFloat(2);
                item.count = c.getInt(3);
                list.add(item);
            }
        } finally {
            c.close();
        }
        return list;
    }

}
