package com.example.mark.popmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.popmovie.model.MovieReview;

import java.util.List;

/**
 * Created by mark on 7/18/17.
 */

public class ReviewListAdapter extends ArrayAdapter<MovieReview> implements AdapterView.OnItemClickListener {

    private Context context;
    private List<MovieReview> values;

    public ReviewListAdapter(Context context, List<MovieReview> values)
    {
        super(context, R.layout.review_list_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.review_list_item, parent, false);

        Log.d("INFO", "REVIEWS ADAPTER LOAD view for values for values[" + position + "] = ");// + values.get(position));

        TextView tv = (TextView) rowView.findViewById(R.id.tv_review_text);

        tv.setText("Review #" + (position+1));
        return rowView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("INFO", "Review onItemClick");
        // new intent to display full review
        Intent intent = new Intent(context, MovieReviewActivity.class);
        intent.putExtra("author", values.get(i).getAuthor());
        intent.putExtra("content", values.get(i).getContent());
        context.startActivity(intent);
    }
}
