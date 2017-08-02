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

/**
 * Created by mark on 7/18/17.
 */

public class TrailerListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

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

        TextView tv = (TextView) rowView.findViewById(R.id.tv_trailer_list_item);

        tv.setText("Trailer #" + (position+1));
        return rowView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url = "https://www.youtube.com/watch?v=" + values[i];
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
