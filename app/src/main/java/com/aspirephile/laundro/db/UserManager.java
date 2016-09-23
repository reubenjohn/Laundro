package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.LaundroContract.User;
import com.aspirephile.laundro.db.tables.ParlayUser;

/**
 * Created by Reuben John on 9/9/2016.
 */
public class UserManager extends TableManager {

    public UserManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(User.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(User.SQL_DELETE_ENTRIES);
    }

    public boolean checkUserAuthenticationResult(@NonNull Cursor c) {
        if (c.getCount() > 0) {
            c.moveToFirst();
            long count = c.getLong(0);
            c.close();
            return count == 1;
        } else {
            return false;
        }
    }

    public LaundroQuery getIsUserAuthenticatedQuery(@NonNull String email) {
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

    public LaundroQuery getUserQuery(@NonNull String email) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(User.TABLE_NAME);
        String query = qb.buildQuery(new String[]{User.COLUMN_NAME_EMAIL, User.COLUMN_NAME_NAME},
                User.COLUMN_NAME_EMAIL + "=?",
                null,
                null,
                null,
                null);
        return new LaundroQuery(dbHelper, query, new String[]{email});
    }

    public ParlayUser getUserFromResult(Cursor c) {
        c.moveToFirst();
        return new ParlayUser(c);
    }
}
