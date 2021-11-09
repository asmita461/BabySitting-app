package com.example.babysittingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.babysittingapp.BabysitterContract.BabysitterEntry;


public class BabysitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BabysitDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "babysit.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BabysitDbHelper}.
     *
     * @param context of the app
     */
    public BabysitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private BabysitDbHelper mDbHelper;

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(BabysitDbHelper.LOG_TAG,".........bEFORE Created.......");
        // Create a String that contains the SQL statement to create the babysitters table
        String SQL_CREATE_BABYSITTERS_TABLE =  "CREATE TABLE " + BabysitterEntry.TABLE_NAME + " ("
                + BabysitterEntry._ID + " INTEGER PRIMARY KEY, "
                +BabysitterEntry.COLUMN_BABYSITTER_NAME + " TEXT NOT NULL, "
                +BabysitterEntry.COLUMN_ADDRESS + " TEXT, "
                +BabysitterEntry.COLUMN_CONTACT + " TEXT, "
                + BabysitterEntry.COLUMN_LATITUDE + " DECIMAL(8,5) NOT NULL, "
                + BabysitterEntry.COLUMN_LONGITUDE + " DECIMAL(9,5) NOT NULL);";



        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BABYSITTERS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
