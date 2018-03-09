package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.popularmovies1.popularmoviesstage2.MoviesAdapter;
import com.udacity.popularmovies1.popularmoviesstage2.ReviewsAdapter;
import com.udacity.popularmovies1.popularmoviesstage2.VideosAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 08/03/2018.
 */
@Module
public class UserInterfaceModule {

    private MoviesAdapter.ListItemClickListener listItemClickListener;
    private VideosAdapter.movieVideoClickListener movieVideoClickListener;
    private int nOfColumnOnGrid;

    public UserInterfaceModule(MoviesAdapter.ListItemClickListener listItemClickListener,
                               VideosAdapter.movieVideoClickListener movieVideoClickListener,
                               int nOfColumnOnGrid){
        this.listItemClickListener = listItemClickListener;
        this.movieVideoClickListener = movieVideoClickListener;
        this.nOfColumnOnGrid = nOfColumnOnGrid;
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager(Context context){
        return new GridLayoutManager(context, nOfColumnOnGrid);
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(Context context){
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Provides
    public MoviesAdapter.ListItemClickListener listItemClickListener() {
        return listItemClickListener;
    }

    @Provides
    public VideosAdapter.movieVideoClickListener movieVideoClickListener(){
        return movieVideoClickListener;
    }
/*
    @Provides
    public MoviesAdapter provideMoviesAdapter(Context context){
        return new MoviesAdapter(listItemClickListener);
    }*/

    /*@Provides
    public VideosAdapter provideVideosAdapter(){
        return new VideosAdapter(movieVideoClickListener);
    }

    @Provides
    public ReviewsAdapter provideReviewsAdapter(){
        return new ReviewsAdapter();
    }*/
}
