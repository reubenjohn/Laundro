package com.aspirephile.laundro.db.tables;

import android.database.Cursor;
import android.util.Log;

public class User {

    public long _id;
    public String email;
    public String name;

    public User(Cursor c) {
        _id = c.getLong(0);
        email = c.getString(1);
        name = c.getString(2);
        Log.d("TAG", toString());
    }

    public String toString() {
        return "{id: " + _id + ", email: " + email + ", name: " + name
                + "}";
    }

}
