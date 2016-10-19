package com.aspirephile.laundro.db;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * Created by Reuben John on 9/9/2016.
 */
public final class LaundroContract {
    private static final String INTEGER_TYPE = "INTEGER";
    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String NOT_NULL = "NOT NULL";
    private static final String UNIQUE = "UNIQUE";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LaundroContract() {
    }

    @NonNull
    private static String separated(String separator, String... elements) {
        String res = "";
        if (elements.length > 0) {
            for (int i = 0; i < elements.length - 1; i++) {
                res += (elements[i] + separator);
            }
            res += elements[elements.length - 1];
        }
        return res;
    }

    private static String COLUMN(String name, String type, String... constraints) {
        return name + " " + type + " " + separated(" ", constraints);
    }

    @NonNull
    private static String COLUMNS(String... columns) {
        return separated(COMMA_SEP, columns);
    }

    private static String PRIMARY_KEY(String... columns) {
        String res = "PRIMARY KEY(";
        res += separated(COMMA_SEP, columns);
        res += ")";
        return res;
    }

    private static String CREATE_TABLE(String name, String columns, String primaryKeys) {
        return "CREATE TABLE " + name + " ( " + columns + COMMA_SEP + primaryKeys + " )";
    }

    private static String DROP_TABLE(String name) {
        return "DROP TABLE " + name;
    }

    /* Inner class that defines the table contents */
    public static final class User implements BaseColumns {
        public static final String COLUMN_NAME_EMAIL = "`email`";
        public static final String COLUMN_NAME_NAME = "`name`";
        static final String TABLE_NAME = "`User`";
        //TODO Ensure distinct emails
        static final String SQL_CREATE_ENTRIES = CREATE_TABLE(
                TABLE_NAME,
                COLUMNS(
                        COLUMN(_ID, INTEGER_TYPE, NOT_NULL, UNIQUE),
                        COLUMN(COLUMN_NAME_EMAIL, TEXT_TYPE, NOT_NULL, UNIQUE),
                        COLUMN(COLUMN_NAME_NAME, TEXT_TYPE, NOT_NULL)
                ),
                PRIMARY_KEY(_ID));
        static final String SQL_DELETE_ENTRIES = DROP_TABLE(TABLE_NAME);
    }

    public static class Service implements BaseColumns {
        static final String TABLE_NAME = "`Service`";
        static final String COLUMN_NAME_NAME = "`name`";
        static final String COLUMN_NAME_CREATED_AT = "`createdAt`";
        static final String COLUMN_NAME_LOCATION = "`location`";
        static final String SQL_CREATE_ENTRIES = CREATE_TABLE(
                TABLE_NAME,
                COLUMNS(
                        COLUMN(_ID, INTEGER_TYPE, NOT_NULL, UNIQUE),
                        COLUMN(COLUMN_NAME_NAME, TEXT_TYPE, NOT_NULL, UNIQUE),
                        COLUMN(COLUMN_NAME_CREATED_AT, INTEGER_TYPE, NOT_NULL),
                        COLUMN(COLUMN_NAME_LOCATION, INTEGER_TYPE, NOT_NULL)
                ),
                PRIMARY_KEY(_ID));
        static final String SQL_DELETE_ENTRIES = DROP_TABLE(TABLE_NAME);
    }
}