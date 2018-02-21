package com.udacity.popularmovies1.popularmoviesstage2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";

    private ImageView poster;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieVoteAverage;
    private TextView movieOverview;

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
        poster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieVoteAverage = findViewById(R.id.movie_vote_average);
        movieOverview = findViewById(R.id.movie_overview);

        //Retrieve the bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Extract the parced movie from bundle
            Movie movie = bundle.getParcelable(Movie.CLASS_STRING_EXTRA);

            //Load backdrop poster
            String imageUrl = URL_BASE_MOVIE_BANNER + movie.getBackdropPath();
            Picasso.with(this).load(imageUrl).into(poster);

            //Load all information on UI components
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
            movieOverview.setText(movie.getOverview());
        }
    }
}
