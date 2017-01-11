package com.example.mark.popmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mark.popmovie.model.Movie;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_COLUMNS = 2;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(getSampleData());
        recyclerView.setAdapter(movieAdapter);



    }


    public ArrayList<Movie> getSampleData()
    {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i=0; i < 12; i++)
        {
            Movie m = new Movie("Bad Santa", 3, "/img/path", "Lying, cheating, stealing Santa " +
                    "Claus. Coming to mall near you.");
            movies.add(m);
        }
        return movies;
    }
}
