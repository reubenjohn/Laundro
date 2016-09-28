package com.aspirephile.laundro.db;

import android.database.Cursor;
import android.database.SQLException;

public interface OnQueryCompleteListener {
    void onQueryComplete(Cursor c, SQLException e);
}
