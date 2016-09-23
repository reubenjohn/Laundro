package com.aspirephile.laundro.db.tables;

import android.database.Cursor;

/**
 * Created by Reuben John on 9/24/2016.
 */
public class Service {
    public final long createdAt;
    public final String name;
    public final String location;
    public final int _id;

    public Service(Cursor c) {
        _id = c.getInt(0);
        createdAt = c.getLong(1);
        name = c.getString(2);
        location = c.getString(3);
    }
}
