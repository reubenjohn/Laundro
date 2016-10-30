package com.aspirephile.laundro.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

import static com.aspirephile.laundro.db.LaundroContract.Review.DESCRIPTION;
import static com.aspirephile.laundro.db.LaundroContract.Review.RATING;
import static com.aspirephile.laundro.db.LaundroContract.Review.SERVICE;
import static com.aspirephile.laundro.db.LaundroContract.Review.TIMESTAMP;
import static com.aspirephile.laundro.db.LaundroContract.Review.USER;
import static com.aspirephile.laundro.db.LaundroContract.Review._ID;

/**
 * Created by Reuben John on 9/24/2016.
 */
public class Review {
    public final long user, service, timestamp;
    public final float rating;
    public final String description;
    public final long _id;

    public Review(long service, long user, long timestamp, float rating, String description) {
        this(-1, service, user, timestamp, rating, description);
    }

    public Review(long id, long service, long user, long timestamp, float rating, String description) {
        this._id = id;
        this.user = user;
        this.service = service;
        this.timestamp = timestamp;
        this.rating = rating;
        this.description = description;
    }

    public Review(Cursor c) {
        _id = c.getInt(0);
        service = c.getLong(1);
        user = c.getLong(2);
        timestamp = c.getLong(3);
        rating = c.getLong(4);
        description = c.getString(5);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (_id != -1)
            values.put(_ID, _id);
        values.put(USER, user);
        values.put(SERVICE, service);
        values.put(TIMESTAMP, timestamp);
        values.put(RATING, rating);
        values.put(DESCRIPTION, description);
        return values;
    }
}
