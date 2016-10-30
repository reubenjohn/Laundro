package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.tables.User;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.User.EMAIL;
import static com.aspirephile.laundro.db.LaundroContract.User.NAME;
import static com.aspirephile.laundro.db.LaundroContract.User.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.User.SQL_DELETE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.User.TABLE_NAME;


public class UserManager extends TableManager {

    UserManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public QueryStatement isUserAuthenticated(@NonNull String email) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{"count(*)"},
                EMAIL + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{email});
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

    public QueryStatement getUser(@NonNull String email) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, EMAIL, NAME},
                EMAIL + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{email});
    }

    public QueryStatement getUser(long _id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, EMAIL, NAME},
                _ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(_id)});
    }

    public User getUserFromResult(Cursor c) {
        c.moveToFirst();
        return new User(c);
    }

    public UpdateStatement updateName(String email, String name) {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        return new UpdateStatement(dbHelper,
                TABLE_NAME,
                values,
                EMAIL + "=?",
                new String[]{email});
    }
}
