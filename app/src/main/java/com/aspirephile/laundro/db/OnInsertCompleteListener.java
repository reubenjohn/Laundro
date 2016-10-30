package com.aspirephile.laundro.db;

import android.database.SQLException;

public interface OnInsertCompleteListener {
    void onInsertComplete(long rowId, SQLException e);
}
