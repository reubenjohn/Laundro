package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.aspirephile.laundro.db.LaundroContract.User;

/**
 * Created by Reuben John on 9/9/2016.
 */
public class UserManager extends TableManager {

    public UserManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(SQLiteDatabase db) {
        db.execSQL(User.SQL_DELETE_ENTRIES);
    }

    public boolean checkUserAuthenticationResult(Cursor c) {
        if (c.getCount() > 0) {
            c.moveToFirst();
            long count = c.getLong(0);
            c.close();
            return count == 1;
        } else {
            return false;
        }
    }

    public LaundroQuery getIsUserAuthenticatedQuery(String email) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(User.TABLE_NAME);
        String query = qb.buildQuery(new String[]{"count(*)"},
                User.COLUMN_NAME_EMAIL + "=?",
                null,
                null,
                null,
                null);
        return new LaundroQuery(dbHelper, query, new String[]{email});
    }

}
