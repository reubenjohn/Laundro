package com.aspirephile.laundro.db.async;

import android.database.SQLException;

public interface OnInsertCompleteListener {
    void onInsertComplete(long rowId, SQLException e);
}
