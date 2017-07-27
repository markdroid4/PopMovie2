package com.example.mark.popmovie;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mark.popmovie.com.example.mark.popmovie.util.JSONHelper;
import com.example.mark.popmovie.com.example.mark.popmovie.util.NetworkUtil;
import com.example.mark.popmovie.model.Movie;
import com.example.mark.popmovie.model.MovieReview;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;
import static android.R.attr.value;
import static java.security.AccessController.getContext;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.toString();

    private LoaderManager.LoaderCallbacks trailersLoaderCallback;
    private LoaderManager.LoaderCallbacks reviewsLoaderCallback;

    private int LOADER_TRAILER_ID = 77;
    private int LOADER_REVIEW_ID = 78;

    private Movie movie;
    private TextView movieTitleText;
    private TextView movieDateText;
    private TextView movieRating;
    private TextView movieOverviewText;
    private TextView trailerHeader;
    private TextView reviewHeader;
    private ImageView imageView;
    private ImageView imageFavView;
    private ExpandableHeightListView trailerListView;
    private ExpandableHeightListView reviewListView;
    private TrailerListAdapter trailerListAdapter;
    private ReviewListAdapter reviewListAdapter;

    //for AsynchTaskLoader
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieTitleText = (TextView) findViewById(R.id.tv_movie_title);
        movieDateText = (TextView) findViewById(R.id.tv_movie_date);
        movieRating = (TextView) findViewById(R.id.tv_movie_rating);
        movieOverviewText = (TextView) findViewById(R.id.tv_overview);
        trailerHeader = (TextView) findViewById(R.id.tv_trailers);
        reviewHeader = (TextView) findViewById(R.id.tv_reviews);
        imageView = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        trailerListView = (ExpandableHeightListView) findViewById(R.id.lv_trailers);
        reviewListView = (ExpandableHeightListView) findViewById(R.id.lv_reviews);
        trailerListView.setExpanded(true);
        reviewListView.setExpanded(true);
        context = getBaseContext();

        Intent caller = getIntent();
        if (caller.hasExtra("MOVIE"))
        {
            movie = caller.getParcelableExtra("MOVIE");
            if (movie != null) {
                movieOverviewText.setText(movie.getOverview());
                movieTitleText.setText(movie.getTitle());
                movieRating.setText(movie.getRating());
                movieDateText.setText(movie.getYear());
                Picasso.with(this).load(movie.getImgPrefix("original") + movie.getImagePath())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView);
                trailerHeader.setText("Movie Trailers:");
                reviewHeader.setText("Movie Reviews:");
            }
        }

        LoaderManager loaderManager = getSupportLoaderManager();

        trailersLoaderCallback = new LoaderManager.LoaderCallbacks<String[]>() {
            @Override
            public Loader<String[]> onCreateLoader(int id, Bundle args) {
                Log.d("INFO", "Trailer - onCreateLoader");

                return new AsyncTaskLoader<String[]>(context) {


                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public String[] loadInBackground() {

                        Log.d("INFO", "Loading in Background");
                        String[] trailers = null;
                        URL url;
                        String urlString = "https://api.themoviedb.org/3/movie/"+ movie.getMovieId() + "?api_key=" + getContext().getString(R.string.api_key) +
                                "&append_to_response=videos";
                        Log.d("INFO","url=" + urlString);
                        String result;
                        try {
                            url = new URL(urlString);
                            result = NetworkUtil.getResponseFromHttpUrl(url);
                            trailers = JSONHelper.createTrailerListFromJSON(result);

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        //Log.d("INFO","trailers: " + trailers);

                        return trailers;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader loader, String[] data) {
                //load up the UI with the contents
                trailerListAdapter = new TrailerListAdapter(getBaseContext(), data);
                trailerListView.setAdapter(trailerListAdapter);
                trailerListView.setOnItemClickListener(trailerListAdapter);
            }

            @Override
            public void onLoaderReset(Loader loader) {

            }
        };

        reviewsLoaderCallback = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {
            @Override
            public Loader<List<MovieReview>> onCreateLoader(int id, Bundle args) {
                Log.d("INFO", "onCreateLoader");

                return new AsyncTaskLoader<List<MovieReview>>(context) {


                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public List<MovieReview> loadInBackground() {

                        Log.d("INFO", "MovieReview Loading in Background");
                        List<MovieReview> reviews = null;
                        URL url;
                        String urlString = "https://api.themoviedb.org/3/movie/"+ movie.getMovieId() + "?api_key=" + getContext().getString(R.string.api_key) +
                                "&append_to_response=reviews";
                        Log.d("INFO","url=" + urlString);
                        String result;
                        try {
                            url = new URL(urlString);
                            result = NetworkUtil.getResponseFromHttpUrl(url);
                            reviews =  JSONHelper.getMovieReviewsListFromJSON(result);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        catch (JSONException je)
                        {
                            je.printStackTrace();
                        }

                        return reviews;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> data) {
                //load up the UI with the contents
                reviewListAdapter = new ReviewListAdapter(getBaseContext(), data);
                reviewListView.setAdapter(reviewListAdapter);
                reviewListView.setOnItemClickListener(reviewListAdapter);
            }

            @Override
            public void onLoaderReset(Loader loader) {

            }
        };

        //get loader if it already exists
        //init loader if not loaded yet
        Loader<String[]> trailerLoader = loaderManager.getLoader(LOADER_TRAILER_ID);
        Loader<List<MovieReview>> reviewLoader = loaderManager.getLoader(LOADER_REVIEW_ID);

        if (trailerLoader == null)
        {
            Log.d("DEBUG", "trailerLoader == null --> init");
            loaderManager.initLoader(LOADER_TRAILER_ID, savedInstanceState, trailersLoaderCallback);
        } else {
            Log.d("DEBUG", "trailerLoader != null --> restart");
            loaderManager.restartLoader(LOADER_TRAILER_ID, savedInstanceState, trailersLoaderCallback);
        }

        if (reviewLoader == null)
        {
            Log.d("DEBUG", "reviewLoader == null --> init");
            loaderManager.initLoader(LOADER_REVIEW_ID, savedInstanceState, reviewsLoaderCallback);
        } else {
            Log.d("DEBUG", "reviewLoader != null --> restart");
            loaderManager.restartLoader(LOADER_REVIEW_ID, savedInstanceState, reviewsLoaderCallback);
        }
    }

    /**
     * Mark a movie as a fav
     * @param v
     */
    public void favoriteClicked(View v)
    {
        Log.d("INFO", "fav star clicked");
    }

}
