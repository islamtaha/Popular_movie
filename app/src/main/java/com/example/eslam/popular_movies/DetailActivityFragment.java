package com.example.eslam.popular_movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eslam.popular_movies.adapters.ReviewAdapter;
import com.example.eslam.popular_movies.adapters.TrailerAdapter;
import com.example.eslam.popular_movies.models.Movie;
import com.example.eslam.popular_movies.models.Review;
import com.example.eslam.popular_movies.models.ReviewResponse;
import com.example.eslam.popular_movies.models.Trailer;
import com.example.eslam.popular_movies.models.TrailerResponse;
import com.example.eslam.popular_movies.services.MovieClient;
import com.example.eslam.popular_movies.services.MovieService;
import com.example.eslam.popular_movies.tasks.ManageFavouritesAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.android.popular_movies.tasks.ManageFavouritesAsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.movie_title)
    TextView movieTitleTextView;

    @Bind(R.id.movie_desc)
    TextView movieOverviewTextView;

    @Bind(R.id.movie_rating)
    TextView movieRatingTextView;

    @Bind(R.id.movie_release_date)
    TextView movieReleaseDateTextView;

    @Bind(R.id.movie_poster)
    ImageView moviePosterImageView;

    @Bind(R.id.favorite)
    Button favButton;

    public Movie movieDetail;

    public ReviewAdapter reviewAdapter;

    public TrailerAdapter trailerAdapter;

    public MovieService movieService;

    private String firstTrailer ;

    private ShareActionProvider mShareActionProvider;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();


        if (args == null) {
            rootView.setVisibility(View.INVISIBLE);
            return rootView;
        }
        rootView.setVisibility(View.VISIBLE);
        movieDetail = args.getParcelable("MOVIE");
        if (movieDetail != null) {
            Log.i("id",movieDetail.getId()+"");
            movieTitleTextView.setText(movieDetail.getOriginalTitle());
            movieOverviewTextView.setText(movieDetail.getOverview());
            movieRatingTextView.setText(""+movieDetail.getVoteCount());
            movieReleaseDateTextView.setText(movieDetail.getReleaseDate());
            Picasso.with(getContext()).load(movieDetail.getImageFullURL()).into(moviePosterImageView);
        }
        new ManageFavouritesAsyncTask(getActivity(), movieDetail, false, favButton).execute();

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ManageFavouritesAsyncTask(getActivity(), movieDetail,true, favButton).execute();
            }
        });
        final List<Review> reviews = new ArrayList<>();
        final List<Trailer> trailers = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(getActivity(), reviews);
        trailerAdapter = new TrailerAdapter(getActivity(), trailers);

        ListView reviewList = (ListView) rootView.findViewById(R.id.review_list);
        reviewList.setAdapter(reviewAdapter);

        ListView trailerList = (ListView) rootView.findViewById(R.id.trailer_list);
        trailerList.setAdapter(trailerAdapter);

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String youtubeVideoId = trailers.get(position).getKey();
                String videoURI = "http://www.youtube.com/watch?v=" + youtubeVideoId;
                Log.i("you",videoURI);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURI));
                startActivity(i);
            }
        });

        movieService = MovieClient.getClient().create(MovieService.class);


        fetchReviews();
        fetchTrailers();



        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail, menu);
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        Log.i("share provider menu: ", mShareActionProvider==null?"y":"n");



        if (firstTrailer != null) {
            Log.i("inflate menu: ", "menu");
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }


    private void fetchReviews() {
        Call<ReviewResponse> reviewCall = movieService.getMovieReviews(movieDetail.getId(), "7e285d3a20c96679bef948b35520c274");
        Log.i("movie is null: ",movieDetail==null?"y":"n");
        reviewCall.enqueue(new Callback<ReviewResponse>() {

            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                List<Review> reviews = response.body().getResults();
               reviewAdapter.clear();
               for (Review review : reviews) {
                   reviewAdapter.add(review);
               }

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Throw up", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchTrailers() {
        Call<TrailerResponse> trailersListCall = movieService.getMovieTrailers(movieDetail.getId(), "7e285d3a20c96679bef948b35520c274");
        trailersListCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<Trailer> trailers = response.body().getResults();

                if(trailers.size() > 0){
                    firstTrailer =  trailers.get(0).getKey();
                }
                Log.i("firstTrailer is null: ", firstTrailer==null?"y":"n");
                Log.i("share provider trail: ", mShareActionProvider==null?"y":"n");


                if (mShareActionProvider != null) {
                    Log.i("inflate menu: ", "menu");
                    mShareActionProvider.setShareIntent(createShareForecastIntent());
                }

                trailerAdapter.clear();
                for (Trailer trailer : trailers) {
                    trailerAdapter.add(trailer);
                }

                Log.i("isnull-trailer",response.body()==null?"y":"n");
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Throw up", Toast.LENGTH_LONG).show();
            }
        });
    }


    private Intent createShareForecastIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"check this Trailer! https://www.youtube.com/watch?v="+firstTrailer);
        return intent;
    }

}