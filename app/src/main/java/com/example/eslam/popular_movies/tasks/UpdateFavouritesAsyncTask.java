package com.example.eslam.popular_movies.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.eslam.popular_movies.R;
import com.example.eslam.popular_movies.db.DbUtils;
import com.example.eslam.popular_movies.db.MovieContracts;
import com.example.eslam.popular_movies.models.Movie;

/**
 * Created by eslam on 19/09/2016.
 */

public class UpdateFavouritesAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private Movie movie;
    private Boolean isAlreadyFavouite;
    private Button favButton;


    public UpdateFavouritesAsyncTask(Context mContext, Movie movie, Boolean isAlreadyFavouite, Button favButton) {
        this.mContext = mContext;
        this.movie = movie;
        this.isAlreadyFavouite = isAlreadyFavouite;
        this.favButton = favButton;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!isAlreadyFavouite) {
            ContentValues contentValues = DbUtils.toContentValue(movie);
            Log.i("contentValues is null: ", contentValues==null?"y":"n");
            mContext.getContentResolver().insert(MovieContracts.MOVIES_TABLE.CONTENT_URI, contentValues);

        } else {
            mContext.getContentResolver().delete(
                    MovieContracts.MOVIES_TABLE.CONTENT_URI,
                    MovieContracts.MOVIES_TABLE._ID + " = ?",
                    new String[]{String.valueOf(movie.getId())}
            );
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        int toastStrRes;
        if (!isAlreadyFavouite) {
            toastStrRes = R.string.added_fav_msg;
            favButton.setText(mContext.getString(R.string.mark_unfavorite));
        } else {
            toastStrRes = R.string.remove_fav_msg;
            favButton.setText(mContext.getString(R.string.mark_favorite));
        }
        Toast.makeText(mContext, mContext.getString(toastStrRes), Toast.LENGTH_SHORT).show();
    }
}
