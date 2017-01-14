package com.example.mark.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark.popmovie.model.Movie;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    private TextView overview;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        overview = (TextView) findViewById(R.id.tv_sample);
//      imageView = (ImageView) findViewById(R.id.iv_movie_poster);
        Intent caller = getIntent();
        if (caller.hasExtra("MOVIE"))
        {
            movie = (Movie) caller.getParcelableExtra("MOVIE");
            if (movie != null)
                overview.setText(movie.getOverview());
        }
    }
}