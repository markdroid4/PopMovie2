package com.example.mark.popmovie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mark on 1/11/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    MovieAdapter()
    {

    }


    class MovieViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView = null;
        public MovieViewHolder(View view)
        {
            super(view);

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

    }
}
