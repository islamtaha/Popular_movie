package com.example.eslam.popular_movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eslam.popular_movies.R;
import com.example.eslam.popular_movies.models.Trailer;

import java.util.List;

/**
 * Created by eslam on 20/08/2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerText.setText(getContext().getString(R.string.trailer,(position + 1)));
        return convertView;
    }
}
