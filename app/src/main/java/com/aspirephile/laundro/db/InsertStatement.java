package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.shared.debug.Logger;

public class InsertStatement {
    public final ContentValues values;
    private final LaundroDb dbHelper;
    private final String tableName;
    private final String nullColumnHack;
    private Logger l = new Logger(InsertStatement.class);

    InsertStatement(LaundroDb dbHelper, String tableName, ContentValues values) {
        this.dbHelper = dbHelper;
        this.tableName = tableName;
        this.values = values;
        this.nullColumnHack = null;
    }

    public long execute() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        l.d("Executing update statement");
        long rowId = db.insert(tableName, nullColumnHack, values);
        l.d("Update statement affected " + rowId + " rows");
        return rowId;
    }

    public void executeInBackground(OnInsertCompleteListener onInsertCompleteListener) {
        AsyncInsertTask asyncInsertTask = new AsyncInsertTask(onInsertCompleteListener);
        asyncInsertTask.execute(this);
    }
}
