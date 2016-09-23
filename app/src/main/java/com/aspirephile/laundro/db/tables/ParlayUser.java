package com.aspirephile.laundro.db.tables;

import android.database.Cursor;

import java.sql.SQLException;

import static com.aspirephile.laundro.db.LaundroContract.User.COLUMN_NAME_EMAIL;
import static com.aspirephile.laundro.db.LaundroContract.User.COLUMN_NAME_NAME;

public class ParlayUser {

    public String email;
    public String name;
    public String reputation;

    public ParlayUser(Cursor c) {
        email = c.getString(c.getColumnIndex(COLUMN_NAME_EMAIL));
        name = c.getString(c.getColumnIndex(COLUMN_NAME_NAME));
    }

    public String toString() {
        return "{email: " + email + ", name: " + name
                + /*", lName: " + lName +*/ ", reputation: " + reputation
                + "}";
    }

}
