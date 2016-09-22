package com.example.eslam.popular_movies.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.example.eslam.popular_movies.R;
import com.example.eslam.popular_movies.db.DbUtils;
import com.example.eslam.popular_movies.models.Movie;

/**
 * Created by eslam on 20/08/2016.
 */
public class ManageFavouritesAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private Movie movie;
    private Boolean performAction;
    private Button favButton;

    public ManageFavouritesAsyncTask(Context mContext, Movie movie, Boolean performAction, Button favButton) {
        this.mContext = mContext;
        this.movie = movie;
        this.performAction = performAction;
        this.favButton = favButton;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Log.i("k",movie==null?"y":"n");
        return DbUtils.isFavourite(mContext, movie.getId());
    }


    @Override
    protected void onPostExecute(Boolean isFavorited) {
        if (performAction) {
            new UpdateFavouritesAsyncTask(mContext, movie, isFavorited, favButton).execute();
        } else {
            if (isFavorited) {
                favButton.setText(mContext.getString(R.string.mark_unfavorite));
            } else {
                favButton.setText(mContext.getString(R.string.mark_favorite));
            }
        }
    }

}
