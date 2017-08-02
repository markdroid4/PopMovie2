package com.example.mark.popmovie.com.example.mark.popmovie.util;

import android.util.Log;

import com.example.mark.popmovie.MovieDetailActivity;
import com.example.mark.popmovie.model.Movie;
import com.example.mark.popmovie.model.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/18/17.
 */

public class JSONHelper {

    private static final String TAG = JSONHelper.class.toString();

    /**
     * Returns a list of youtube keys uniquely identifying movie trailers on youtube
     * @param json
     * @return
     * @throws JSONException
     */
    public static String[] createTrailerListFromJSON(String json) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject videoObj = jsonObject.getJSONObject("videos");
        JSONArray videoResults = videoObj.getJSONArray("results");
        String[] trailers = new String[videoResults.length()];
        Log.d("INFO", "number of trailers"  + videoResults.length());
        for (int i=0; i<videoResults.length(); i++)
        {
            JSONObject obj = (JSONObject) videoResults.get(i);
            String key = obj.getString("key");
            trailers[i] = key;
        }

        return trailers;
    }

    /**
     * Returns a list of reviews
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<MovieReview> getMovieReviewsListFromJSON(String json) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(json);
        List<MovieReview> reviews = new ArrayList<>();

        JSONObject reviewObj = jsonObject.getJSONObject("reviews");
        JSONArray movieReviews = reviewObj.getJSONArray("results");
        for (int i=0; i<movieReviews.length(); i++)
        {
            JSONObject obj = (JSONObject) movieReviews.get(i);
            MovieReview review = new MovieReview(
                    obj.getString("id"),
                    obj.getString("author"),
                    obj.getString("content"),
                    obj.getString("url"));
            reviews.add(review);
        }

        return reviews;
    }
}
