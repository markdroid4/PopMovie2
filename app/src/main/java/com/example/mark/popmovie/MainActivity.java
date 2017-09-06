package com.example.mark.popmovie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.popmovie.com.example.mark.popmovie.util.DBHelper;
import com.example.mark.popmovie.model.Movie;
import com.example.mark.popmovie.model.MovieContentProvider;
import com.example.mark.popmovie.model.MovieReaderContract;

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

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.toString();
    private final int ID_CURSOR_LOADER = 99;

    private Spinner sortSpin;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieCursorAdapter movieCursorAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();
    private TextView errorTextView;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);

        sortSpin = (Spinner) findViewById(R.id.spin_sort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpin.setOnItemSelectedListener(this);
        sortSpin.setAdapter(adapter);

        if (!hasOnlineAccess()) {
            // Spinner is selected as soon as it is loaded, no need to load here
            errorTextView = (TextView) findViewById(R.id.tv_no_network);
            errorTextView.setVisibility(View.VISIBLE);
            sortSpin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.d("TAG", "LOADER onLoaderFinished");
        if (recyclerView == null) {
            int grid_columns = getResources().getInteger(R.integer.gallery_columns);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), grid_columns);
            recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        if (cursor != null) {
            cursor.moveToFirst();
            Log.d("INFO", "cursor First " + cursor.getString(
                            cursor.getColumnIndex(
                                MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE)));
            movieCursorAdapter = new MovieCursorAdapter(getBaseContext(), cursor);
            recyclerView.setAdapter(movieCursorAdapter);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d("TAG", "LOADER onCreateLoader");

        String selection = "fav=?";
        String[] selectionArgs = new String[]{"1"};

        Loader loader = new CursorLoader(this,
                MovieContentProvider.getContentUri(),
                null,
                selection,
                selectionArgs,
                MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE);
        return loader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("TAG", "LOADER onLoaderReset");
        if (movieCursorAdapter!=null) {
            //movieCursorAdapter.swapCursor();
        }

    }

    /**
     * Check if the app is online
     * @return
     */
    public boolean hasOnlineAccess()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * Load the movie API in a background thread, via AsynchTask
     * @param context
     * @param endPoint
     */
    public void loadMovies(Context context, String endPoint)
    {
        movies = new ArrayList<>();
        LoadMoviesTask getMovies = new LoadMoviesTask(context, endPoint);
        getMovies.execute();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        Log.d(TAG, "SPINNER SELECTED");
        String endPoint;
        String item = (String) adapterView.getItemAtPosition(pos);
        if (item.equals("Most Popular")) {
            endPoint = getString(R.string.endpoint_most_popular);
            loadMovies(this, endPoint);
        } else if (item.equals("Top Rated")) {
            endPoint = getString(R.string.endpoint_top_rated);
            loadMovies(this, endPoint);
        }
        else if (item.equals("Favorites")) {
            Log.d(TAG, "SPINNER FAVORITES SELECTED");
            getSupportLoaderManager().initLoader(ID_CURSOR_LOADER, null, this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public ArrayList<Movie> createMovieListFromJSON(String json) throws JSONException
    {
        JSONObject obj = new JSONObject(json);
        JSONArray results = (JSONArray) obj.get("results");
        Log.d(TAG, "Movies found: " + results.length());

        for (int i=0; i<results.length(); i++)
        {
            JSONObject jsonMovie = (JSONObject) results.get(i);
            String img = jsonMovie.getString("poster_path");
            String title = jsonMovie.getString("original_title");
            String overview = jsonMovie.getString("overview");
            String rating = jsonMovie.getString("vote_average");
            String releaseDate = jsonMovie.getString("release_date");
            String movieId = jsonMovie.getString("id");

            Movie movie = new Movie(title, rating, img, overview, releaseDate, 0);
            movie.setMovieId(movieId);
            movies.add(movie);
        }
        return movies;

    }

    class LoadMoviesTask extends AsyncTask {

        private Context context;
        // /movie/popular
        // /movie/top_rated
        //private final String TMDB_URL = "https://api.themoviedb.org/3/discover/movie?";
        private final String TMDB_URL = "https://api.themoviedb.org/3/movie/";
        private String apiKey;
        private String endPoint = "";

        final String PARAM_API = "api_key";

        public LoadMoviesTask(Context context, String endPoint) {
            super();
            this.context = context;
            this.endPoint = endPoint;
            apiKey = context.getString(R.string.api_key);
        }


        /**
         * Builds the URL used to query TMDB.
         *
         * @param endPoint
         * @return The URL to use
         */
        public URL buildUrl(String endPoint) {
            Uri builtUri = Uri.parse(TMDB_URL + endPoint).buildUpon()
                    .appendQueryParameter(PARAM_API, apiKey)
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
        public String getResponseFromHttpUrl(URL url) throws IOException {
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


        @Override
        protected Object doInBackground(Object[] objects) {

            String result = null;
            try {

                URL url = buildUrl(endPoint);
                result = getResponseFromHttpUrl(url);
                movies = createMovieListFromJSON(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "returning json: " + result);

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            int grid_columns = getResources().getInteger(R.integer.gallery_columns);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, grid_columns);
            recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            movieAdapter = new MovieAdapter(context, movies);
            recyclerView.setAdapter(movieAdapter);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Object o) {
            super.onCancelled(o);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
}
