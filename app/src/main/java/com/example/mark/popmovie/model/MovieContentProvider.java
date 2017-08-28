package com.example.mark.popmovie.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mark.popmovie.com.example.mark.popmovie.util.DBHelper;

import static android.R.attr.id;
import static android.util.Log.d;
import static com.example.mark.popmovie.model.MovieReaderContract.MovieEntry.CONTENT_AUTHORITY;
import static com.example.mark.popmovie.model.MovieReaderContract.MovieEntry.CONTENT_PATH;

/**
 * Created by mark on 8/13/17.
 */

public class MovieContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private SQLiteDatabase db;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // match a whole list of movies
        uriMatcher.addURI(CONTENT_AUTHORITY, CONTENT_PATH, MOVIES);
        // match a single row
        uriMatcher.addURI(CONTENT_AUTHORITY, CONTENT_PATH + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    //only one table to get in this app
    public static Uri getContentUri() {
        return CONTENT_URI.buildUpon()
                .appendPath("movies")
                .build();
    }

    //only one table to get in this app
    public static Uri buildContentUri(String id) {
        return CONTENT_URI.buildUpon()
                .appendPath("movies")
                .appendPath(id)
                .build();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        db = new DBHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int id = 0;
        switch(match) {
            case MOVIE_WITH_ID:
                id = db.update(MovieReaderContract.MovieEntry.TABLE_NAME,
                                    contentValues,
                                    s,
                                    strings);
                if(id < 0) {
                    throw new SQLException("Failed to update row  " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder){

        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Log.d("INFO", "QUERY URI MATCH =" + match);


        Cursor cursor;
        switch(match) {
            case MOVIES:
                cursor = db.query(MovieReaderContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE
                );
                break;
            case MOVIE_WITH_ID:
                cursor = db.query(MovieReaderContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        d("INFO", "CP insert");

        // TODO: int match = sUriMatcher.match(uri);
        int match = MOVIE_WITH_ID;

        Uri returnUri;
        switch(match) {
            case MOVIE_WITH_ID:
                long id = db.insertWithOnConflict(MovieReaderContract.MovieEntry.TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_FAIL
                );
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContentProvider.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("INFO", "CP INSERT movie, id=" + id);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
