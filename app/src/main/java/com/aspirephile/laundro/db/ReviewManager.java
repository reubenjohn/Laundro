package com.aspirephile.laundro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.aspirephile.laundro.db.async.InsertStatement;
import com.aspirephile.laundro.db.async.QueryStatement;
import com.aspirephile.laundro.db.tables.Review;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.aspirephile.laundro.db.LaundroContract.Review.DESCRIPTION;
import static com.aspirephile.laundro.db.LaundroContract.Review.RATING;
import static com.aspirephile.laundro.db.LaundroContract.Review.SERVICE;
import static com.aspirephile.laundro.db.LaundroContract.Review.SQL_CREATE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Review.SQL_DELETE_ENTRIES;
import static com.aspirephile.laundro.db.LaundroContract.Review.TABLE_NAME;
import static com.aspirephile.laundro.db.LaundroContract.Review.TIMESTAMP;
import static com.aspirephile.laundro.db.LaundroContract.Review.USER;

public class ReviewManager extends TableManager {
    ReviewManager(LaundroDb dbHelper) {
        super(dbHelper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDestroy(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);

    }

    public QueryStatement getAllReviews() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, SERVICE, USER, TIMESTAMP, RATING, DESCRIPTION},
                null,
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, null);
    }

    public QueryStatement getAllReviews(long serviceId, long userId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, SERVICE, USER, TIMESTAMP, RATING, DESCRIPTION},
                SERVICE + "=? AND " + USER + "=?",
                null,
                null,
                null,
                null);

        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(serviceId), String.valueOf(userId)});
    }

    @NonNull
    public List<Review> getRowsFromResult(@NonNull Cursor c) {
        ArrayList<Review> list = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                Review row = new Review(c);
                list.add(row);
            }
        } finally {
            c.close();
        }
        return list;
    }

    public QueryStatement getReview(int _id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{_ID, SERVICE, USER, TIMESTAMP, RATING, DESCRIPTION},
                LaundroContract.Service._ID + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(_id)});
    }

    public Review getRowFromResult(@NonNull Cursor c) {
        List<Review> services = getRowsFromResult(c);
        if (services.size() > 0)
            return services.get(0);
        else
            return null;
    }

    public InsertStatement insertReview(Review review) {
        ContentValues values = review.getContentValues();
        return new InsertStatement(dbHelper, TABLE_NAME, values);
    }

    public QueryStatement getAverageReview(long serviceId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        String query = qb.buildQuery(new String[]{"avg(" + RATING + ")"},
                SERVICE + "=?",
                null,
                null,
                null,
                null);
        return new QueryStatement(dbHelper, query, new String[]{String.valueOf(serviceId)});
    }

    public float getAverageRatingFromCursor(Cursor c) {
        c.moveToFirst();
        if (c.getCount() > 0)
            return c.getFloat(0);
        else
            return 0.0f;
    }
}
