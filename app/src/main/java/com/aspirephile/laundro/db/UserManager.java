package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.aspirephile.laundro.db.LaundroContract.User;

/**
 * Created by Reuben John on 9/9/2016.
 */
public class UserManager {
    private final LaundroDb dbHelper;

    public UserManager(LaundroDb dbHelper) {
        this.dbHelper = dbHelper;
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
