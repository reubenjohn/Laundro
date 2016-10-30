package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.shared.debug.Logger;

public class UpdateStatement {
    public final ContentValues values;
    private final LaundroDb dbHelper;
    private final String tableName;
    private final String whereClause;
    private final String[] whereArgs;
    private Logger l = new Logger(UpdateStatement.class);

    UpdateStatement(LaundroDb dbHelper, String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        this.dbHelper = dbHelper;
        this.tableName = tableName;
        this.values = values;
        this.whereClause = whereClause;
        this.whereArgs = whereArgs;
    }

    public int execute() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        l.d("Executing update statement");
        int count = db.update(tableName, values, whereClause, whereArgs);
        l.d("Update statement affected " + count + " rows");
        return count;
    }

    public void executeInBackground(OnUpdateCompleteListener onQueryCompleteListener) {
        AsyncUpdateTask asyncUpdateTask = new AsyncUpdateTask(onQueryCompleteListener);
        asyncUpdateTask.execute(this);
    }
}
