package com.udacity.popularmovies1.popularmoviesstage2;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.popularmovies1.popularmoviesstage2.data.MoviesContract;
import com.udacity.popularmovies1.popularmoviesstage2.model.ApiMoviesModel;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MainActivity.class.getSimpleName();

    private String API_KEY = "";

    private final int NUMBER_OF_COLUMNS = 2;

    private Call<ApiMoviesModel> callMovies;
    private RetrofitApiInterface apiModel;

    private MoviesAdapter movieAdapter;

    private List<Movie> moviesList;
    private List<Movie> moviesFavouriteList;

    //Views
    private ProgressBar loader;
    private RecyclerView moviesContainer;
    private TextView errorMissingApi;
    ///////

    private final static int FAVOURITE_MOVIE_LOADER_ID = 3654;

    enum EMovieList{
        POPULAR,
        TOP_RATED,
        FAVOURITES
    }
    EMovieList movieListType;
    boolean moviesFavouriteLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieve the views
        loader = findViewById(R.id.loader_pb);
        moviesContainer = findViewById(R.id.movies_gv);
        errorMissingApi = findViewById(R.id.error_message);

        //Load API KEY from resources
        API_KEY = getResources().getString(R.string.api_key);

        if(!API_KEY.isEmpty()) {

            //Create and the layout manager of the recycler view
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, NUMBER_OF_COLUMNS);
            moviesContainer.setLayoutManager(gridLayoutManager);

            //Create and set the adapter of the recycler view
            movieAdapter = new MoviesAdapter(this, this);
            moviesContainer.setAdapter(movieAdapter);

            //getLoaderManager().initLoader(FAVOURITE_MOVIE_LOADER_ID, null, this);

            //Create the retrofit, used for retrieve and parse JSON of movies
            Retrofit retrofit = RetrofitServices.getRetrofitInstance();
            apiModel = retrofit.create(RetrofitApiInterface.class);

            if (savedInstanceState != null){
                if (savedInstanceState.containsKey("movie_type")){
                    movieListType = (EMovieList)savedInstanceState.getSerializable("movie_type");
                }
            }
        }
        else
        {
            showErrorMessage();
        }
    }



    /*
    Get the movies list sorted by Top Rated
     */
    private void getMoviesSortedByTopRated(){
        if (apiModel == null) return;
        setShowLoader(true);
        callMovies = apiModel.topRatedsList(API_KEY);
        retrieveMovies();
        movieListType = EMovieList.TOP_RATED;
    }

    /*
    Get the movies list sorted by Popular
     */
    private void getMoviesSortedByPopular(){
        if (apiModel == null) return;
        setShowLoader(true);
        callMovies = apiModel.popularsList(API_KEY);
        retrieveMovies();
        movieListType = EMovieList.POPULAR;
    }

    /*
    Get the list of favourites movies
     */
    private void getMoviesFavourites() {
        moviesList = moviesFavouriteList;
        movieAdapter.swapMoviesList(moviesList);
        movieListType = EMovieList.FAVOURITES;
        if (moviesFavouriteLoaded) setShowLoader(false);
    }

    /*
    Using Retrofit enqueue method retrieve the movies list automatically on a background thread
     */
    private void retrieveMovies(){
        try {
            callMovies.enqueue(new Callback<ApiMoviesModel>() {
                @Override
                public void onResponse(Call<ApiMoviesModel> call, Response<ApiMoviesModel> response) {
                    try {
                        if (response.errorBody() != null){
                            String err = "Response error";
                            Log.d(TAG, err);
                            Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                            return;
                        }

                        moviesList = response.body().getResults();
                        setShowLoader(false);
                        //Set the retrieve list of movies into the adapter of GridView
                        movieAdapter.swapMoviesList(moviesList);
                    } catch (Exception ex){
                        Log.d(TAG, ex.getMessage());
                        Toast.makeText(MainActivity.this, "Unexpected message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiMoviesModel> call, Throwable t) {

                }
            });
        }
        catch (Exception ex){
            String errorMessage = "Unexpected error: " + ex.getMessage();
            Log.d(TAG, errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void showErrorMessage(){
        moviesContainer.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.INVISIBLE);
        errorMissingApi.setVisibility(View.VISIBLE);
    }

    /*
    When app is loading movies a progress bar is shoed and the grid is hidden, and when movies are loaded grid is visible and progress bar is hidden
     */
    private void setShowLoader(boolean shows){
        if (shows){
            loader.setVisibility(View.VISIBLE);
            moviesContainer.setVisibility(View.INVISIBLE);
        }
        else{
            loader.setVisibility(View.INVISIBLE);
            moviesContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.spinner_movie_sort));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sortType = adapterView.getItemAtPosition(i).toString();
                if (sortType.equals(getResources().getString(R.string.sort_popular))) {
                    getMoviesSortedByPopular();
                }
                else if(sortType.equals(getResources().getString(R.string.sort_top_rated))) {
                    getMoviesSortedByTopRated();
                }
                else if(sortType.equals(getResources().getString(R.string.movie_details_favourites_label))){
                    getMoviesFavourites();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(adapter);

        if (movieListType != null) {
            switch (movieListType) {
                case POPULAR:
                    spinner.setSelection(0);
                    break;
                case TOP_RATED:
                    spinner.setSelection(1);
                    break;
                case FAVOURITES:
                    spinner.setSelection(2);
                    break;
            }
        }

        return true;
    }

    @Override
    public void onListItemClick(int positionClicked) {
        //Create the intent for open new activity
        Intent intent = new Intent(this, MovieDetailsActivity.class);

        //Parse the selected movie and parce it into the bundle which is passed to details activity
        Movie movie = moviesList.get(positionClicked);
        Bundle b = new Bundle();
        b.putParcelable(Movie.CLASS_STRING_EXTRA, movie);
        intent.putExtras(b);

        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch (i){
            case FAVOURITE_MOVIE_LOADER_ID:
                moviesFavouriteLoaded = false;
                CursorLoader loader = new CursorLoader(this,
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        MoviesContract.MoviesEntry.COLUMN_TITLE);
                return loader;

            default:
                throw new RuntimeException("Loader Not Implemented: " + i);
        }
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            moviesFavouriteList = new ArrayList<Movie>();

            for (int i = 0; i < cursor.getCount(); i++) {

                Movie favouriteMovie = new Movie();
                favouriteMovie.setId(cursor.getLong(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID_MOVIE)));
                favouriteMovie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE)));
                favouriteMovie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER)));
                favouriteMovie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS)));
                favouriteMovie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USER_RATING)));
                favouriteMovie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMNS_RELEASE_DATE)));
                favouriteMovie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH)));
                favouriteMovie.setRuntime(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_DURATION)));

                moviesFavouriteList.add(favouriteMovie);

                cursor.moveToNext();
            }

            moviesFavouriteLoaded = true;
            setShowLoader(false);
            if (movieListType == EMovieList.FAVOURITES) getMoviesFavourites();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesFavouriteList = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("movie_type", movieListType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieListType == EMovieList.FAVOURITES) setShowLoader(true);

        //if (getLoaderManager().getLoader(FAVOURITE_MOVIE_LOADER_ID) == null) {
            getLoaderManager().initLoader(FAVOURITE_MOVIE_LOADER_ID, null, this);
        //} else {
        //    getLoaderManager().restartLoader(FAVOURITE_MOVIE_LOADER_ID, null, this);
        //}
    }
}
