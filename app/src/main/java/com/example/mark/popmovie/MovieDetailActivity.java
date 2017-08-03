package com.example.mark.popmovie;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark.popmovie.com.example.mark.popmovie.util.DBHelper;
import com.example.mark.popmovie.com.example.mark.popmovie.util.JSONHelper;
import com.example.mark.popmovie.com.example.mark.popmovie.util.NetworkUtil;
import com.example.mark.popmovie.model.Movie;
import com.example.mark.popmovie.model.MovieReaderContract;
import com.example.mark.popmovie.model.MovieReview;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


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
                movieRating.setText(movie.getRating() + "/10");
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

        insertMovieInDb();
        int fav = getFavorite();
        setFavView(fav);

        //get loader if it already exists
        //init loader if not loaded yet
        Loader<String[]> trailerLoader = loaderManager.getLoader(LOADER_TRAILER_ID);
        Loader<List<MovieReview>> reviewLoader = loaderManager.getLoader(LOADER_REVIEW_ID);

        if (trailerLoader == null) {
            loaderManager.initLoader(LOADER_TRAILER_ID, savedInstanceState, trailersLoaderCallback);
        } else {
            loaderManager.restartLoader(LOADER_TRAILER_ID, savedInstanceState, trailersLoaderCallback);
        }

        if (reviewLoader == null) {
            loaderManager.initLoader(LOADER_REVIEW_ID, savedInstanceState, reviewsLoaderCallback);
        } else {
            loaderManager.restartLoader(LOADER_REVIEW_ID, savedInstanceState, reviewsLoaderCallback);
        }
    }

    private void insertMovieInDb()
    {
        SQLiteDatabase db = new DBHelper(getBaseContext()).getWritableDatabase();
        Cursor cursor = db.query(MovieReaderContract.MovieEntry.TABLE_NAME,
                null,
                "ID=?",
                new String[] {movie.getMovieId()},
                null,
                null,
                MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE
        );

        if (cursor!=null && cursor.getCount()>0) {
            //movie already in db
            //do nothing
            Log.d("INFO", "INSERT METHOD - Movie already in DB");
        }
        else {
            //add movie to db
            Log.d("INFO", "Adding new movie to db: " + movie.getTitle());
            SQLiteDatabase dbInsert = new DBHelper(getBaseContext()).getWritableDatabase();

            ContentValues insertValues = new ContentValues();
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_ID, movie.getMovieId());
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_IMAGE_PATH, movie.getImagePath());
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_FAV, 0);
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_SUMMARY, movie.getOverview());
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_RATING, movie.getRating());
            insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());

            long rows = dbInsert.insert(MovieReaderContract.MovieEntry.TABLE_NAME,
                    null,
                    insertValues
            );

            Log.d("INFO", "INSERT METHOD - Inserted movie, rows=" + rows);
            dbInsert.close();
        }
        cursor.close();
        db.close();
    }


    private int getFavorite()
    {
        SQLiteDatabase db = new DBHelper(getBaseContext()).getReadableDatabase();
        Cursor cursor = db.query(MovieReaderContract.MovieEntry.TABLE_NAME,
            new String[] { MovieReaderContract.MovieEntry.COLUMN_NAME_ID, MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE, MovieReaderContract.MovieEntry.COLUMN_NAME_FAV },
            "fav=? and id=?",
            new String[] {"1", movie.getMovieId()},
            null,
            null,
            MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE
        );

        if (cursor!=null && cursor.getCount()>0) {
            Log.d("INFO", "getFavorite() cursorCount = " + cursor.getCount());
            if (cursor.moveToFirst()) {
                Log.d("INFO", "This movie is FAV: " + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
            }
            cursor.close();
            db.close();
            return 1;
        }
        else {
            Log.d("INFO", "This movie is NOT FAV: " + movie.getMovieId());
            cursor.close();
            db.close();
            return 0;
        }
    }

    private void dumpDB()
    {
        SQLiteDatabase db = new DBHelper(getBaseContext()).getReadableDatabase();
        Cursor cursor = db.query(MovieReaderContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieReaderContract.MovieEntry.COLUMN_NAME_TIMESTAMP
        );

        if (cursor != null && cursor.getCount()>0) {
            Log.d("INFO", "DB DUMP movies= " + cursor.getCount());

            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE));
                String movieId = cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.COLUMN_NAME_ID));
                String imagePath = cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.COLUMN_NAME_IMAGE_PATH));
                int fav = cursor.getInt(cursor.getColumnIndex(MovieReaderContract.MovieEntry.COLUMN_NAME_FAV));
                Log.d("INFO", "Title: " + title + ", ID: " + movieId + " img: " + imagePath + ", fav: " + fav);
            }

            cursor.close();
        }
        else {
            Log.d("INFO", "DB empty");
        }
        db.close();
    }

    /**
     * Mark a movie as a fav
     * @param v
     */
    public void favoriteClicked(View v)
    {
        Log.d("INFO", "fav star clicked");
        int fav = getFavorite();
        Log.d("INFO", "getFavorite()=" + fav);
        toggleFavorite(fav);
        dumpDB();
    }

    private void toggleFavorite(int fav)
    {
        if (fav == 0)
            fav=1;
        else if (fav == 1)
            fav=0;

        Log.d("INFO", "inside toggleFavorite setting fav to "+fav+" for movieId " + movie.getMovieId());
        SQLiteDatabase db = new DBHelper(getBaseContext()).getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        insertValues.put(MovieReaderContract.MovieEntry.COLUMN_NAME_FAV, fav);
        //TODO: add all columns here for fav display on MainActivity

        setFavView(fav);

        int rows = db.update(MovieReaderContract.MovieEntry.TABLE_NAME,
                insertValues,
                "ID=?",
                new String[]{movie.getMovieId()}
                );

        Log.d("INFO", "Update fav, rows=" + rows);
        db.close();
    }

    private void setFavView(int fav)
    {
        imageFavView = (ImageView) findViewById(R.id.fav_button);
        Drawable drawable = getDrawable(R.drawable.ic_star_black_24dp);
        if (fav == 0) {
            drawable = getDrawable(R.drawable.ic_star_border_black_24dp);
        }
        imageFavView.setImageDrawable(drawable);
    }

}
