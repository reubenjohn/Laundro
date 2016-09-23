package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by Reuben John on 9/23/2016.
 */
public class AsyncQueryTask extends AsyncTask<LaundroQuery, Void, Cursor> {
    private final OnQueryCompleteListener onQueryCompleteListener;

    public AsyncQueryTask(@NonNull OnQueryCompleteListener onQueryCompleteListener) {
        this.onQueryCompleteListener = onQueryCompleteListener;
    }

    @Override
    protected Cursor doInBackground(LaundroQuery... params) {
        return params[0].query();
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        onQueryCompleteListener.onQueryComplete(cursor);
    }
}
