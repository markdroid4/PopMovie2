package com.example.mark.popmovie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

import static android.R.string.no;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int GRID_COLUMNS = 2;

    private Spinner sortSpin;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sortSpin = (Spinner) findViewById(R.id.spin_sort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpin.setOnItemSelectedListener(this);
        sortSpin.setAdapter(adapter);

        loadMovies(this, "popular");

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

        Log.d("INFO", "SPINNER SELECTED");
        String endPoint="";
        String item = (String) adapterView.getItemAtPosition(pos);
        if (item.equals("Most Popular")) {
            endPoint = "popular";
        } else {
            endPoint = "top_rated";
        }
        loadMovies(this, endPoint);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("INFO", "nothing selected");
    }

    public ArrayList<Movie> getSampleData()
    {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i=0; i < 240; i++)
        {
            Movie m = new Movie("Bad Santa", 3.8f, "/img/path", "Lying, cheating, stealing Santa " +
                    "Claus. Coming to mall near you.");
            movies.add(m);
        }
        return movies;
    }

    public ArrayList<Movie> createMovieListFromJSON(String json) throws JSONException
    {
        JSONObject obj = new JSONObject(json);
        JSONArray results = (JSONArray) obj.get("results");
        Log.d("INFO", "Movies found: " + results.length());

        for (int i=0; i < results.length(); i++)
        {
            JSONObject jsonMovie = (JSONObject) results.get(i);
            String img = jsonMovie.getString("poster_path");
            String title = jsonMovie.getString("title");
            String overview = jsonMovie.getString("overview");

            Movie movie = new Movie(title, 3.8f, img, overview);
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
                    //.appendQueryParameter(PARAM_SORT, sortBy)
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
            Log.d("INFO", "Loading URL: " + url);
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

            Log.d("INFO", "returning json: " + result);

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, GRID_COLUMNS);
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
