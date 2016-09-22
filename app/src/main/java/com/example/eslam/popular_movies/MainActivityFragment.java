package com.example.eslam.popular_movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.eslam.popular_movies.adapters.MovieAdapter;
import com.example.eslam.popular_movies.models.Movie;
import com.example.eslam.popular_movies.models.MoviesResponse;
import com.example.eslam.popular_movies.services.MovieClient;
import com.example.eslam.popular_movies.services.MovieService;
import com.example.eslam.popular_movies.tasks.FetchFavouritesAsyncTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    public ProgressDialog dialog;
    private MovieService movieService;
    private List<Movie> movies;
    private String mSortCriteria = "popularity.desc";
    private MenuItem mMenuItemSortPopular;
    private MenuItem mMenuItemSortRating;
    private MenuItem mMenuItemSortFav;
    public static GridView movieGrid;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    public interface BunldeCallback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onPause() {
        super.onPause();
        dialog = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);

        mMenuItemSortPopular = menu.findItem(R.id.action_sort_popular);
        mMenuItemSortRating = menu.findItem(R.id.action_sort_rating);
        mMenuItemSortFav = menu.findItem(R.id.action_sort_favourites);

        if (mSortCriteria.contentEquals("popularity.desc")) {
            if (!mMenuItemSortPopular.isChecked()) {
                mMenuItemSortPopular.setChecked(true);
            }
        } else if (mSortCriteria.contentEquals("vote_average.desc")) {
            if (!mMenuItemSortRating.isChecked()) {
                mMenuItemSortRating.setChecked(true);
            }
        } else if (mSortCriteria.contentEquals("favourites")) {
            if (!mMenuItemSortFav.isChecked()) {
                mMenuItemSortFav.setChecked(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                if (isOnline()) {
                    mSortCriteria = "popularity.desc";
                    fetchMovies(mSortCriteria);
                    if (!mMenuItemSortPopular.isChecked()) {
                        mMenuItemSortPopular.setChecked(true);
                    }
                }
                return true;
            case R.id.action_sort_rating:
                if (isOnline()) {
                    mSortCriteria = "vote_average.desc";
                    fetchMovies(mSortCriteria);
                    if (!mMenuItemSortRating.isChecked()) {
                        mMenuItemSortRating.setChecked(true);
                    }
                }

                return true;
            case R.id.action_sort_favourites:
                mSortCriteria = "favourites";
                if (!mMenuItemSortFav.isChecked()) {
                    mMenuItemSortFav.setChecked(true);
                }
                new FetchFavouritesAsyncTask(getContext(), mMovieAdapter, movies).execute();

                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movies = new ArrayList<>();
        movieService = MovieClient.getClient().create(MovieService.class);
        if (isOnline()) {
            fetchMovies("popularity.desc");
        }


        movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).onItemSelected(movie);
            }
        });


        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(getContext(), getString(R.string.internet_conn_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void fetchMovies(String sort_order) {


        Call<MoviesResponse> moviesCall = movieService.getMovies(sort_order, "7e285d3a20c96679bef948b35520c274");
        moviesCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> results = response.body().getResults();

                Log.d("kk", "Number of movies received: " + results.size());

                mMovieAdapter = new MovieAdapter(getActivity(), results);
                movieGrid.setAdapter(mMovieAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.i("hhh", t.getMessage());
            }
        });
    }


}