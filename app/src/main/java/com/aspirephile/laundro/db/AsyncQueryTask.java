package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

class AsyncQueryTask extends AsyncTask<QueryStatement, Void, Cursor> {
    private final OnQueryCompleteListener onQueryCompleteListener;
    private SQLException e;

    AsyncQueryTask(@NonNull OnQueryCompleteListener onQueryCompleteListener) {
        this.onQueryCompleteListener = onQueryCompleteListener;
    }

    @Override
    protected Cursor doInBackground(QueryStatement... params) {
        try {
            return params[0].execute();
        } catch (SQLException e) {
            this.e = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        onQueryCompleteListener.onQueryComplete(cursor, e);
    }
}
