package com.udacity.popularmovies1.popularmoviesstage2;

import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies1.popularmoviesstage2.data.MoviesContract;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiReviewsModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiVideosModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;
import com.udacity.popularmovies1.popularmoviesstage2.model.Review;
import com.udacity.popularmovies1.popularmoviesstage2.model.Video;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailsActivity extends AppCompatActivity
        implements VideosAdapter.movieVideoClickListener, View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";

    private Movie movie;

    //Views
    private ProgressBar loader;
    private NestedScrollView movieDetailsContainer;
    private ImageView poster;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieDuration;
    private TextView movieVoteAverage;
    private ImageView movieFavourite;
    private TextView movieOverview;

    private ProgressBar loaderVideos;
    private RecyclerView videosContainer;

    private ProgressBar loaderReviews;
    private RecyclerView reviewsContainer;
    ///////

    private VideosAdapter videosAdapter;
    private ReviewsAdapter reviewsAdapter;

    private Call<Movie> callMovie;
    private Call<ApiVideosModel> callVideos;
    private Call<ApiReviewsModel> callReviews;
    private RetrofitApiInterface apiModel;
    private List<Video> videos;
    private List<Review> reviews;

    private long movieIdIntoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Retrieve the UI components
        loader = findViewById(R.id.loader_pb);
        movieDetailsContainer = findViewById(R.id.movies_details_container);
        poster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieDuration = findViewById(R.id.movie_duration);
        movieVoteAverage = findViewById(R.id.movie_vote_average);
        movieFavourite = findViewById(R.id.movie_favourite);
        movieOverview = findViewById(R.id.movie_overview);

        loaderVideos = findViewById(R.id.loader_videos_pb);
        videosContainer = findViewById(R.id.videos_container);
        videosContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videosAdapter = new VideosAdapter(this);
        videosContainer.setAdapter(videosAdapter);

        loaderReviews = findViewById(R.id.loader_reviews_pb);
        reviewsContainer = findViewById(R.id.reviews_container);
        reviewsContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsAdapter = new ReviewsAdapter();
        reviewsContainer.setAdapter(reviewsAdapter);


        //Retrieve the bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Extract the parced movie from bundle
            movie = bundle.getParcelable(Movie.CLASS_STRING_EXTRA);

            Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build(),
                    null,
                    null,
                    null,
                    MoviesContract.MoviesEntry.COLUMN_TITLE);


            //Load backdrop poster
            String imageUrl = URL_BASE_MOVIE_BANNER + movie.getPosterPath();
            Picasso.with(this).load(imageUrl).into(poster);

            //Load all information on UI components
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieVoteAverage.setText(String.valueOf(movie.getVoteAverage()) + " / 10");
            movieFavourite.setOnClickListener(this);
            movieOverview.setText(movie.getOverview());


            Retrofit retrofit = RetrofitServices.getRetrofitInstance();
            apiModel = retrofit.create(RetrofitApiInterface.class);

            if (cursor.getCount() > 0){
                movieFavourite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                cursor.moveToFirst();
                movieIdIntoDb = cursor.getLong(cursor.getColumnIndex(MoviesContract.MoviesEntry._ID));

                movieDuration.setText(String.valueOf(movie.getRuntime()));
            } else {
                retrieveMovieDetails(movie.getId());
            }

            retrieveVideos(movie.getId());
            retrieveReviews(movie.getId());
        }
    }

    private void retrieveMovieDetails(long movieId){
        setShowLoader(true);
        callMovie = apiModel.movieDetail(movieId, MainActivity.API_KEY);
        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                try {

                    if (response.errorBody() != null){
                        String err = "Response error";
                        Log.d(TAG, err);
                        Toast.makeText(MovieDetailsActivity.this, err, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Movie movieDetail = response.body();
                    movieDuration.setText(String.valueOf(movieDetail.getRuntime()));
                    movie.setRuntime(movieDetail.getRuntime());

                    setShowLoader(false);

                } catch (Exception ex){
                    Log.d(TAG, ex.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Unexpected error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void retrieveVideos(long movieId){
        setShowVideosLoader(true);
        callVideos = apiModel.videosList(movieId, MainActivity.API_KEY);

        callVideos.enqueue(new Callback<ApiVideosModel>() {
            @Override
            public void onResponse(Call<ApiVideosModel> call, Response<ApiVideosModel> response) {
                try {

                    if (response.errorBody() != null){
                        String err = "Response error";
                        Log.d(TAG, err);
                        Toast.makeText(MovieDetailsActivity.this, err, Toast.LENGTH_LONG).show();
                        return;
                    }

                    videos = response.body().getResults();
                    videosAdapter.swapVideos(videos);
                    setShowVideosLoader(false);

                } catch (Exception ex){
                    Log.d(TAG, ex.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Unexpected error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiVideosModel> call, Throwable t) {

            }
        });
    }

    private void retrieveReviews(long movieId){
        setShowReviewsLoader(true);
        callReviews = apiModel.reviewsList(movieId, MainActivity.API_KEY);

        callReviews.enqueue(new Callback<ApiReviewsModel>() {
            @Override
            public void onResponse(Call<ApiReviewsModel> call, Response<ApiReviewsModel> response) {
                try {
                    if (response.errorBody() != null){
                        String err = "Response error";
                        Log.d(TAG, err);
                        Toast.makeText(MovieDetailsActivity.this, err, Toast.LENGTH_LONG).show();
                        return;
                    }

                    reviews = response.body().getResults();
                    reviewsAdapter.swapReviews(reviews);
                    setShowReviewsLoader(false);

                } catch (Exception ex){
                    Log.d(TAG, ex.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Unexpected message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiReviewsModel> call, Throwable t) {

            }
        });
    }

    /*
    When app is loading movies a progress bar is shoed and the grid is hidden, and when movies are loaded grid is visible and progress bar is hidden
     */
    private void setShowLoader(boolean shows){
        if (shows){
            loader.setVisibility(View.VISIBLE);
            movieDetailsContainer.setVisibility(View.INVISIBLE);
        }
        else{
            loader.setVisibility(View.INVISIBLE);
            movieDetailsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setShowVideosLoader(boolean shows){
        if (shows){
            loaderVideos.setVisibility(View.VISIBLE);
            videosContainer.setVisibility(View.INVISIBLE);
        }
        else{
            loaderVideos.setVisibility(View.INVISIBLE);
            videosContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setShowReviewsLoader(boolean shows){
        if (shows){
            loaderReviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.INVISIBLE);
        }
        else{
            loaderReviews.setVisibility(View.INVISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVideoItemClick(int clickedVideoIndex) {
        String videoId = videos.get(clickedVideoIndex).getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    @Override
    public void onClick(View view) {

        //If current movie is not in the preferred movies user list
        if (movieIdIntoDb == 0) {
            ContentValues cv = new ContentValues();

            cv.put(MoviesContract.MoviesEntry.COLUMN_ID_MOVIE, movie.getId());
            cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER, movie.getPosterPath());
            cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            cv.put(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS, movie.getOverview());
            cv.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING, movie.getVoteAverage());
            cv.put(MoviesContract.MoviesEntry.COLUMNS_RELEASE_DATE, movie.getReleaseDate());
            cv.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
            cv.put(MoviesContract.MoviesEntry.COLUMN_DURATION, movie.getRuntime());

            //Add the content values into db through ContentProvider
            Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);

            if (uri != null) {
                movieIdIntoDb = Long.parseLong(uri.getPathSegments().get(1));
                movieFavourite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                Toast.makeText(this, "Movie added to favourite!", Toast.LENGTH_LONG).show();
            }
        } else{
            //If the movie is in the preferred movies user list
            if (getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieIdIntoDb)).build(), null, null) > 0){
                movieIdIntoDb = 0;
                movieFavourite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                Toast.makeText(this, "Movie removed from favourite!", Toast.LENGTH_LONG).show();
            }
        }
        getContentResolver().notifyChange(MoviesContract.MoviesEntry.CONTENT_URI, null);
    }
}
