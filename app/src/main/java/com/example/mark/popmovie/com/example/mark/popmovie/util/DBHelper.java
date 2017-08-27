package com.example.mark.popmovie.com.example.mark.popmovie.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mark.popmovie.model.MovieReaderContract;

/**
 * Created by mark on 7/27/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 5;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieReaderContract.MovieEntry.TABLE_NAME + " (" +
                MovieReaderContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_ID + " TEXT NOT NULL UNIQUE, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_IMAGE_PATH + " TEXT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_FAV + " TINYINT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_SUMMARY + " TEXT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_RATING + " TEXT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                MovieReaderContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("INFO", "onUpgrade called");
        final String SQL_UPGRADE = "DROP TABLE IF EXISTS " + MovieReaderContract.MovieEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_UPGRADE);
        onCreate(sqLiteDatabase);
    }
}
