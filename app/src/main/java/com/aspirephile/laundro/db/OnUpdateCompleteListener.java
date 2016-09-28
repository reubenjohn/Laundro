package com.aspirephile.laundro.db;

import android.database.SQLException;

public interface OnUpdateCompleteListener {
    void onUpdateComplete(int cursor, SQLException e);
}
