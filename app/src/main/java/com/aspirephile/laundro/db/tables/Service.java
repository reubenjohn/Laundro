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
    public final String description;
    public String phone;

    public Service(Cursor c) {
        _id = c.getInt(0);
        name = c.getString(1);
        createdAt = c.getLong(2);
        location = c.getString(3);
        phone = c.getString(4);
        description = c.getString(5);
    }
}
