package com.udacity.popularmovies1.popularmoviesstage2;

import android.os.AsyncTask;
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
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiVideosModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;
import com.udacity.popularmovies1.popularmoviesstage2.model.Video;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";

    private String API_KEY = "";

    //Views
    private ProgressBar loader;
    private NestedScrollView movieDetailsContainer;
    private ImageView poster;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieDuration;
    private TextView movieVoteAverage;
    private TextView movieOverview;

    private ProgressBar videosLoader;
    private RecyclerView videosContainer;
    ///////

    private VideosAdapter videosAdapter;

    private Call<Movie> callMovie;
    private Call<ApiVideosModel> callVideos;
    private RetrofitApiInterface apiModel;
    private List<Video> videos;


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
        movieOverview = findViewById(R.id.movie_overview);

        videosLoader = findViewById(R.id.loader_videos_pb);
        videosContainer = findViewById(R.id.videos_container);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        videosContainer.setLayoutManager(linearLayoutManager);
        videosAdapter = new VideosAdapter();
        videosContainer.setAdapter(videosAdapter);

        //Retrieve the bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setShowLoader(true);

            //Extract the parced movie from bundle
            final Movie movie = bundle.getParcelable(Movie.CLASS_STRING_EXTRA);

            //Load backdrop poster
            String imageUrl = URL_BASE_MOVIE_BANNER + movie.getPosterPath();
            Picasso.with(this).load(imageUrl).into(poster);

            //Load all information on UI components
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieVoteAverage.setText(String.valueOf(movie.getVoteAverage()) + " / 10");
            movieOverview.setText(movie.getOverview());

            API_KEY = getResources().getString(R.string.api_key);



            Retrofit retrofit = RetrofitServices.getRetrofitInstance();
            apiModel = retrofit.create(RetrofitApiInterface.class);

            retrieveMovieDetails(movie.getId());
            retrieveVideos(movie.getId());
        }
    }

    private void retrieveMovieDetails(long movieId){
        callMovie = apiModel.movieDetail(movieId, API_KEY);
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

                    setShowLoader(false);

                } catch (Exception ex){
                    Log.d(TAG, ex.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Unexpected message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void retrieveVideos(long movieId){
        callVideos = apiModel.videosList(movieId, API_KEY);
        setShowVideoLoader(true);

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
                    /*for (int i = 0; i <= 100; i++) {
                        Video video = new Video();
                        video.setName("Video #" + String.valueOf(i));
                        videos.add(video);
                    }*/

                    videosAdapter.swapVideos(videos);
                    setShowVideoLoader(false);
                } catch (Exception ex){
                    Log.d(TAG, ex.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Unexpected message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiVideosModel> call, Throwable t) {

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

    private void setShowVideoLoader(boolean shows) {
        if (shows){
            videosLoader.setVisibility(View.VISIBLE);
            videosContainer.setVisibility(View.INVISIBLE);
        }
        else{
            videosLoader.setVisibility(View.INVISIBLE);
            videosContainer.setVisibility(View.VISIBLE);
        }
    }
}
