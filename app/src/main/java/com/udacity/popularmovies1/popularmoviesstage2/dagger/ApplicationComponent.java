package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.app.Activity;
import android.content.Context;

import com.udacity.popularmovies1.popularmoviesstage2.MainActivity;
import com.udacity.popularmovies1.popularmoviesstage2.MovieDetailsActivity;
import com.udacity.popularmovies1.popularmoviesstage2.MoviesAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 06/03/2018.
 */

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    void inject(MainActivity context);
    void inject(MovieDetailsActivity context);
}
