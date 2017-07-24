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
import android.widget.ListView;
import android.widget.TextView;

import com.example.mark.popmovie.com.example.mark.popmovie.util.JSONHelper;
import com.example.mark.popmovie.com.example.mark.popmovie.util.NetworkUtil;
import com.example.mark.popmovie.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.key;
import static android.R.attr.value;

public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String[]> {

    private final String TAG = MovieDetailActivity.class.toString();
    private int LOADER_TRAILER_ID = 77;

    private Movie movie;
    private TextView movieTitleText;
    private TextView movieDateText;
    private TextView movieRating;
    private TextView movieOverviewText;
    private TextView trailerHeader;
    private ImageView imageView;
    private ImageView imageFavView;
    private ExpandableHeightListView trailerListView;
    private ExpandableHeightListView reviewListView;
    TrailerListAdapter trailerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieTitleText = (TextView) findViewById(R.id.tv_movie_title);
        movieDateText = (TextView) findViewById(R.id.tv_movie_date);
        movieRating = (TextView) findViewById(R.id.tv_movie_rating);
        movieOverviewText = (TextView) findViewById(R.id.tv_overview);
        trailerHeader = (TextView) findViewById(R.id.tv_trailers);
        imageView = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        trailerListView = (ExpandableHeightListView) findViewById(R.id.lv_trailers);
        reviewListView = (ExpandableHeightListView) findViewById(R.id.lv_reviews);
        trailerListView.setExpanded(true);
        reviewListView.setExpanded(true);

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
            }
        }

        LoaderManager loaderManager = getSupportLoaderManager();

        //get loader if it already exists
        //init loader if not loaded yet
        Loader<String[]> trailerLoader = loaderManager.getLoader(LOADER_TRAILER_ID);

        if (trailerLoader == null)
        {
            Log.d("DEBUG", "trailerLoader == null --> init");
            loaderManager.initLoader(LOADER_TRAILER_ID, savedInstanceState, this);
        } else {
            Log.d("DEBUG", "trailerLoader != null --> restart");
            loaderManager.restartLoader(LOADER_TRAILER_ID, savedInstanceState, this);
        }
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {

        Log.d("INFO", "onCreateLoader");

        return new AsyncTaskLoader<String[]>(this) {


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
                        "&append_to_response=videos,reviews";
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
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        //load up the UI with the contents
        trailerListAdapter = new TrailerListAdapter(this.getBaseContext(), data);
        trailerListView.setAdapter(trailerListAdapter);
        trailerListView.setOnItemClickListener(trailerListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

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
