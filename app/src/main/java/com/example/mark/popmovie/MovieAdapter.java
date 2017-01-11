package com.example.mark.popmovie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


                class MovieViewHolder extends RecyclerView.ViewHolder
                {
                    TextView textView;
                    public MovieViewHolder(View view)
                    {
                        super(view);
                        textView = (TextView) view.findViewById(R.id.tv_movie_text);
                    }

                    public void bind(String s)
                    {
                        textView.setText(s);
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
        holder.bind(movies.get(position).getTitle());
    }
}
