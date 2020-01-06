package com.example.fajar.reisenote.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReiseDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reiseeee.db";

    public static final int VERSION = 1;

    public ReiseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String CREATE_TABLE = "CREATE TABLE " + ReiseContract.ReiseEntry.TABLE_NAME + " (" +
                ReiseContract.ReiseEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ReiseContract.ReiseEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                ReiseContract.ReiseEntry.COLUMN_FAV + " INT , " +
                ReiseContract.ReiseEntry.COLUMN_STARRED + " INT , " +
                ReiseContract.ReiseEntry.COLUMN_STORY + " INT , " +
                ReiseContract.ReiseEntry.COLUMN_POEM + " INT , " +
                ReiseContract.ReiseEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                ReiseContract.ReiseEntry.COLUMN_IMAGE + " TEXT," +
                ReiseContract.ReiseEntry.COLUMN_LAST_UPDATED + " INT)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReiseContract.ReiseEntry.TABLE_NAME);
        onCreate(db);
    }
}
