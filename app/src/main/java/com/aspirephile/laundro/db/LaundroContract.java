package com.aspirephile.laundro.db;

import android.provider.BaseColumns;

/**
 * Created by Reuben John on 9/9/2016.
 */
public final class LaundroContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LaundroContract() {
    }

    private static final String TEXT_TYPE = "varchar(50)";
    private static final String COMMA_SEP = ",";

    /* Inner class that defines the table contents */
    public static final class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NAME = "name";
        //TODO Ensure distinct emails
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " ( " +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_EMAIL + " " + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NAME + " " + TEXT_TYPE + " )";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE " + TABLE_NAME;
    }
}