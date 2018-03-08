package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.popularmovies1.popularmoviesstage2.MoviesAdapter;
import com.udacity.popularmovies1.popularmoviesstage2.ReviewsAdapter;
import com.udacity.popularmovies1.popularmoviesstage2.VideosAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 08/03/2018.
 */

@Singleton
@Component(modules = { UserInterfaceModule.class, ApplicationModule.class })
public interface UserInterfaceComponent {
    MoviesAdapter getMovieAdapter();
    VideosAdapter getVideosAdapter();
    ReviewsAdapter getReviewsAdapter();
    GridLayoutManager getGridLayoutManager();
    LinearLayoutManager getLinearLayoutManager();
}
