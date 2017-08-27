package com.example.mark.popmovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark.popmovie.model.Movie;
import com.example.mark.popmovie.model.MovieContentProvider;
import com.example.mark.popmovie.model.MovieReaderContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by mark on 1/11/17.
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieViewHolder> {

    private final String TAG = MovieCursorAdapter.class.toString();

    private Context context;
    private Cursor movieCursor;

    MovieCursorAdapter(Context context, Cursor cursor)
    {
        this.context = context;
        if (cursor == null)
            throw new IllegalArgumentException("Movie list cannot be null");
        else
            Log.d("INFO", "found # of fav movies: " + cursor.getCount());
        movieCursor = cursor;
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        Movie movie;

        public MovieViewHolder(View view)
        {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        public void bind(Movie m) {
            movie = m;
            Picasso.with(context).load(m.getImgPrefix("w342") + m.getImagePath())
                    .placeholder(R.mipmap.ic_launcher) //will not work without placeholder set -??
                    .into(imageView);
        }

        @Override
        public void onClick(View view) {
            Intent movieDetailIntent = new Intent(view.getContext(), MovieDetailActivity.class);
            movieDetailIntent.putExtra("MOVIE", movie);
            view.getContext().startActivity(movieDetailIntent);
        }
    }

    @Override
    public int getItemCount() {
        return movieCursor.getCount();
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

        Movie movie = new Movie();
        if (movieCursor.moveToPosition(position)) {
            movie.setMovieId(
                    movieCursor.getString(
                    movieCursor.getColumnIndex(
                            MovieReaderContract.MovieEntry.COLUMN_NAME_ID)));
            movie.setTitle(
                    movieCursor.getString(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE)));
            movie.setOverview(
                    movieCursor.getString(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_SUMMARY)));
            movie.setImagePath(
                    movieCursor.getString(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_IMAGE_PATH)));
            movie.setRating(
                    movieCursor.getString(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_RATING)));
            movie.setReleaseDate(
                    movieCursor.getString(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)));
            movie.setYear(movie.getReleaseDate().substring(0, 4));
            movie.setFavorite(
                    movieCursor.getInt(
                            movieCursor.getColumnIndex(
                                    MovieReaderContract.MovieEntry.COLUMN_NAME_FAV)));

        }

        Log.d("INFO" , "onBindViewHolder movieId =====" + movie.getMovieId());
        holder.bind(movie);
    }
}
