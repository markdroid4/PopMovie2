package com.example.mark.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark.popmovie.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.toString();

    private Movie movie;
    private TextView movieTitleText;
    private TextView movieDateText;
    private TextView movieOverviewText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieTitleText = (TextView) findViewById(R.id.tv_movie_title);
        movieDateText = (TextView) findViewById(R.id.tv_movie_date);
        movieOverviewText = (TextView) findViewById(R.id.tv_overview);
        imageView = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        Intent caller = getIntent();
        if (caller.hasExtra("MOVIE"))
        {
            movie = (Movie) caller.getParcelableExtra("MOVIE");
            if (movie != null) {
                movieOverviewText.setText(movie.getOverview());
                movieTitleText.setText(movie.getTitle() + " (Rating: " + movie.getRating() + ")");
                movieDateText.setText(movie.getReleaseDate());
                Picasso.with(this).load(movie.getImgPrefix("w500") + movie.getImagePath())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView);
            }
        }
    }
}
