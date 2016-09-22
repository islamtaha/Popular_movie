package com.example.eslam.popular_movies.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.eslam.popular_movies.MainActivityFragment;
import com.example.eslam.popular_movies.adapters.MovieAdapter;
import com.example.eslam.popular_movies.db.MovieContracts;
import com.example.eslam.popular_movies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FetchFavouritesAsyncTask extends AsyncTask<Void, Void, List<Movie>> {

    private static final String[] MOVIE_COLUMNS = {
            MovieContracts.MOVIES_TABLE._ID,
            MovieContracts.MOVIES_TABLE.COLUMN_TITLE,
            MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW,
            MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE
    };

    private MovieAdapter mMovieAdapter;

    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_OVERVIEW = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_VOTE = 4;
    public static final int COL_DATE = 5;

    private Context mContext;
    private List<Movie> mMovies;

    public FetchFavouritesAsyncTask(Context context, MovieAdapter movieAdapter, List<Movie> movies) {
        mContext = context;
        mMovieAdapter = movieAdapter;
        mMovies = movies;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getInt(COL_ID),
                        cursor.getString(COL_TITLE),
                        cursor.getString(COL_OVERVIEW),
                        cursor.getString(COL_IMAGE),
                        cursor.getDouble(COL_VOTE),
                        cursor.getString(COL_DATE));
                Log.e("movie","hi");
                Log.e("movie",movie.getOriginalTitle());
                results.add(movie);
                Log.i("Movie in favourites: ", movie==null?"y":"n");
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Log.e("cursor","hi");
        Cursor cursor = mContext.getContentResolver().query(
                MovieContracts.MOVIES_TABLE.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        Log.i("Cursor is null: ", cursor==null?"y":"n");
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            mMovies = new ArrayList<>();
            mMovies.addAll(movies);
            for(int i = 0; i < movies.size(); i++){
                Log.i("Movie is: ", movies.get(i).getImageFullURL());
            }
            mMovieAdapter = new MovieAdapter(mContext, mMovies);
            MainActivityFragment.movieGrid.setAdapter(mMovieAdapter);
        }
    }
}