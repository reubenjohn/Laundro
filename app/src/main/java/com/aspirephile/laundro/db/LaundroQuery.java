package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.shared.debug.Logger;

/**
 * Created by Reuben John on 9/23/2016.
 */
public class LaundroQuery {
    private final LaundroDb dbHelper;
    private final String query;
    private final String[] selectionArgs;
    private Logger l = new Logger(LaundroQuery.class);

    public LaundroQuery(LaundroDb dbHelper, String sql, String[] selectionArgs) {
        this.dbHelper = dbHelper;
        this.query = sql;
        this.selectionArgs = selectionArgs;
    }

    public Cursor query() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        l.d("Executing query: " + query);
        return db.rawQuery(query, selectionArgs);
    }

    public void queryInBackground(OnQueryCompleteListener onQueryCompleteListener) {
        AsyncQueryTask asyncQueryTask = new AsyncQueryTask(onQueryCompleteListener);
        asyncQueryTask.execute(this);
    }
}
