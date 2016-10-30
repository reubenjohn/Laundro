package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.shared.debug.Logger;

import java.util.Arrays;

public class QueryStatement {
    private final LaundroDb dbHelper;
    private final String query;
    private final String[] selectionArgs;
    private Logger l = new Logger(QueryStatement.class);

    QueryStatement(LaundroDb dbHelper, String sql, String[] selectionArgs) {
        this.dbHelper = dbHelper;
        this.query = sql;
        this.selectionArgs = selectionArgs;
    }

    public Cursor execute() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        l.d("Executing query: " + query + " with arguments: " + Arrays.toString(selectionArgs));
        return db.rawQuery(query, selectionArgs);
    }

    public void queryInBackground(OnQueryCompleteListener onQueryCompleteListener) {
        AsyncQueryTask asyncQueryTask = new AsyncQueryTask(onQueryCompleteListener);
        asyncQueryTask.execute(this);
    }
}
