package com.example.eslam.popular_movies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.eslam.popular_movies.R;
import com.example.eslam.popular_movies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends BaseAdapter {
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;


    }

    @Override
    public int getCount() {

        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
        moviePoster.setBackgroundResource(R.mipmap.ic_launcher);
        Picasso.with(context).load(movie.getImageFullURL()).into(moviePoster);



        Log.i("icon",movie.getImageFullURL());
        return convertView;
    }
}
