package com.example.eslam.popular_movies.services;


import com.example.eslam.popular_movies.models.MoviesResponse;
import com.example.eslam.popular_movies.models.ReviewResponse;
import com.example.eslam.popular_movies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("3/discover/movie")
    Call<MoviesResponse> getMovies(@Query("sort_by") String sortBy , @Query("api_key") String api_key);

    @GET("3/movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String api_key);

    @GET("3/movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String api_key);
}