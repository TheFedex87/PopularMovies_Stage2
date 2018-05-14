package com.udacity.popularmovies1.popularmoviesstage2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.popularmovies1.popularmoviesstage2.dagger.ApplicationModule;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.DaggerRetrofitApiInterfaceComponent;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.DaggerUserInterfaceComponent;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.RetrofitApiInterfaceComponent;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.UserInterfaceComponent;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.UserInterfaceModule;
import com.udacity.popularmovies1.popularmoviesstage2.data.MoviesContract;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiReviewsModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiVideosModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;
import com.udacity.popularmovies1.popularmoviesstage2.model.Review;
import com.udacity.popularmovies1.popularmoviesstage2.model.Video;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity
        implements VideosAdapter.movieVideoClickListener, View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";

    private Movie movie;

    private int[] movieDetailsContainerPosition = null;

    //Views
    @BindView(R.id.loader_pb) ProgressBar loader;
    @BindView(R.id.movies_details_container) NestedScrollView movieDetailsContainer;
    @BindView(R.id.movie_poster) ImageView poster;
    @BindView(R.id.movie_title_container) ImageView movieTitleContainer;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.movie_duration) TextView movieDuration;
    @BindView(R.id.movie_vote_average) TextView movieVoteAverage;
    @BindView(R.id.movie_favourite) ImageView movieFavourite;
    @BindView(R.id.movie_overview) TextView movieOverview;

    @BindView(R.id.loader_videos_pb) ProgressBar loaderVideos;
    @BindView(R.id.videos_container) RecyclerView videosContainer;

    @BindView(R.id.loader_reviews_pb) ProgressBar loaderReviews;
    @BindView(R.id.reviews_container) RecyclerView reviewsContainer;
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

    @Inject Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        MyApp.appComponent().inject(this);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(null, this, 0))
                .build();

        //Retrieve the UI components
        ButterKnife.bind(this);

        videosContainer.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        videosAdapter = userInterfaceComponent.getVideosAdapter();
        videosContainer.setAdapter(videosAdapter);

        reviewsContainer.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        reviewsAdapter = userInterfaceComponent.getReviewsAdapter();
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

            RetrofitApiInterfaceComponent daggerRetrofitApiInterfaceComponent = DaggerRetrofitApiInterfaceComponent.builder().applicationModule(new ApplicationModule(this)).build();

            //Load backdrop poster
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                movieTitleContainer.setTransitionName("movieTransition");

            ActivityCompat.postponeEnterTransition(this);
            //supportPostponeEnterTransition();

            String imageBackdropUrl = URL_BASE_MOVIE_BANNER + movie.getBackdropPath();
            daggerRetrofitApiInterfaceComponent.getPicasso().load(imageBackdropUrl).noFade().into(movieTitleContainer, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    ActivityCompat.startPostponedEnterTransition(MovieDetailsActivity.this);
                    //supportStartPostponedEnterTransition();
                }

                @Override
                public void onError() {
                    ActivityCompat.startPostponedEnterTransition(MovieDetailsActivity.this);
                    //supportStartPostponedEnterTransition();
                }
            });

            //Load poster poster
            String imagePosterUrl = URL_BASE_MOVIE_BANNER + movie.getPosterPath();
            daggerRetrofitApiInterfaceComponent.getPicasso().load(imagePosterUrl).into(poster);





            //Load all information on UI components
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieVoteAverage.setText(String.valueOf(movie.getVoteAverage()) + " / 10");
            movieFavourite.setOnClickListener(this);
            movieOverview.setText(movie.getOverview());

            apiModel = daggerRetrofitApiInterfaceComponent.getRetrofitApiInterface();

            if (cursor.getCount() > 0){
                movieFavourite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                cursor.moveToFirst();
                movieIdIntoDb = cursor.getLong(cursor.getColumnIndex(MoviesContract.MoviesEntry._ID));

                movieDuration.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_DURATION))));
            } else {
                //retrieveMovieDetails(movie.getId());
            }

            if (savedInstanceState == null) {
                retrieveVideos(movie.getId());
                retrieveReviews(movie.getId());
            } else {
                if (savedInstanceState.containsKey("videos_list")){
                    videos = new ArrayList(Arrays.asList(savedInstanceState.getParcelableArray("videos_list")));
                    videosAdapter.swapVideos(videos);
                }
                if (savedInstanceState.containsKey("reviews_list")){
                    reviews = new ArrayList(Arrays.asList(savedInstanceState.getParcelableArray("reviews_list")));
                    reviewsAdapter.swapReviews(reviews);
                }
                if (savedInstanceState.containsKey("scroll_position")){
                    int[] positions = savedInstanceState.getIntArray("scroll_position");
                    movieDetailsContainer.setScrollX(positions[0]);
                    movieDetailsContainer.setScrollY(positions[1]);
                }
            }
        }
    }

    private void retrieveMovieDetails(long movieId){
        //setShowLoader(true);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the selected movie list in order to show it again after the activity is recreated
        outState.putIntArray("scroll_position", new int[]{ movieDetailsContainer.getScrollX(), movieDetailsContainer.getScrollY()});
        outState.putParcelableArray("videos_list", videos.toArray(new Video[videos.size()]));
        outState.putParcelableArray("reviews_list", reviews.toArray(new Review[reviews.size()]));
    }
}
