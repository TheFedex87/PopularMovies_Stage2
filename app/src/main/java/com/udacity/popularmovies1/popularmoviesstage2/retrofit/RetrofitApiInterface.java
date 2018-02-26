package com.udacity.popularmovies1.popularmoviesstage2.retrofit;

import com.udacity.popularmovies1.popularmoviesstage2.model.ApiMoviesModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiReviewsModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiVideosModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;
import com.udacity.popularmovies1.popularmoviesstage2.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by federico.creti on 16/02/2018.
 */

public interface RetrofitApiInterface {
    @GET("movie/top_rated")
    Call<ApiMoviesModel> topRatedsList(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<ApiMoviesModel> popularsList(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> movieDetail(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ApiVideosModel> videosList(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ApiReviewsModel> reviewsList(@Path("id") long id, @Query("api_key") String apiKey);
}
