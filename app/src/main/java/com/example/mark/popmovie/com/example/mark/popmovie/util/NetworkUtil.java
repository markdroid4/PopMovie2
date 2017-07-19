package com.example.mark.popmovie.com.example.mark.popmovie.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.mark.popmovie.R;
import com.example.mark.popmovie.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mark on 7/13/17.
 */

public class NetworkUtil {

    private static final String TAG = "INFO";

    private static final String PARAM_API = "api_key";
    private static final String TMDB_URL = "https://api.themoviedb.org/3/movie/";

    /**
     * Builds a TMDB URL.
     *
     * @param endPoint
     * @return The URL to use
     */
    public static URL buildUrl(Context context, String endPoint) {
        Uri builtUri = Uri.parse(TMDB_URL + endPoint).buildUpon()
                .appendQueryParameter(PARAM_API, context.getString(R.string.api_key))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d(TAG, "Loading URL: " + url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }




}
