package com.example.mark.popmovie;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.popmovie.model.Movie;

import java.util.ArrayList;

/**
 * Created by mark on 1/11/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;

    MovieAdapter(ArrayList<Movie> movies)
    {
        if (movies == null)
            throw new IllegalArgumentException("Movie list cannot be null");
        this.movies = movies;
    }


                class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
                {
                    TextView textView;
                    Movie movie;

                    public MovieViewHolder(View view)
                    {
                        super(view);
                        textView = (TextView) view.findViewById(R.id.tv_movie_text);
                        view.setOnClickListener(this);
                    }

                    public void bind(Movie m)
                    {
                        textView.setText(m.getTitle() + "(" + m.getRating() + ")");
                        movie = m;
                    }

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                        Intent movieDetailIntent = new Intent(view.getContext(), MovieDetailActivity.class);
                        movieDetailIntent.putExtra("MOVIE", movie);
                        view.getContext().startActivity(movieDetailIntent);
                    }
                }



    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Inflate the view
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflatedView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.tv_movie, parent, false);

        MovieViewHolder movieViewHolder = new MovieViewHolder(inflatedView);
        return movieViewHolder;
    }

    /**
     * Bind the data associated with the model
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }
}
