package com.example.eslam.popular_movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eslam.popular_movies.R;
import com.example.eslam.popular_movies.models.Review;

import java.util.List;

/**
 * Created by eslam on 20/08/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }

        TextView authorText = (TextView)convertView.findViewById(R.id.author_title);
        authorText.setText(review.getAuthor());

        TextView reviewText = (TextView)convertView.findViewById(R.id.review_content);
        reviewText.setText(review.getContent());


        return convertView;
    }
}