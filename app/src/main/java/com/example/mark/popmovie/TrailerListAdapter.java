package com.example.mark.popmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by mark on 7/18/17.
 */

public class TrailerListAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] values;

    public TrailerListAdapter(Context context, String[] values)
    {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.list_item, parent, false);

        Log.d("INFO", "ADAPTER LOAD view for values[" + position + "] = " + values[position]);

        TextView tv = (TextView) rowView.findViewById(R.id.tv_trailer_list_item);

        tv.setText(values[position]);
        return rowView;
    }

}
