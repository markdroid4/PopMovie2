package com.example.mark.popmovie.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mark on 7/27/17.
 */

public class MovieReaderContract {



    private MovieReaderContract() {}

    public class MovieEntry implements BaseColumns
    {
        public static final String CONTENT_AUTHORITY = "com.example.mark.popmovie";
        public static final String CONTENT_PATH = "movies";


        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGE_PATH = "image_path";
        public static final String COLUMN_NAME_FAV = "fav";
        public static final String COLUMN_NAME_SUMMARY = "summary";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

}
