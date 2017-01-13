package com.example.mark.popmovie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_COLUMNS = 2;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadMoviesTask getMovies = new LoadMoviesTask(this);
        getMovies.execute();



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
        private final String TMDB_URL = "https://api.themoviedb.org/3/discover/movie?";
        private String apiKey;


        final String PARAM_API = "api_key";
        final String PARAM_SORT = "sort_by";
        final String sortBy = "vote_average.desc";

        public LoadMoviesTask(Context context) {
            super();
            this.context = context;
            apiKey = context.getString(R.string.api_key);
        }


        /**
         * Builds the URL used to query TMDB.
         *
         * @param sortBy the criteria
         * @return The URL to use fo
         */
        public URL buildUrl(String sortBy) {
            Uri builtUri = Uri.parse(TMDB_URL).buildUpon()
                    .appendQueryParameter(PARAM_API, apiKey)
                    .appendQueryParameter(PARAM_SORT, sortBy)
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

                URL url = buildUrl(sortBy);

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
            movieAdapter = new MovieAdapter(movies);
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
