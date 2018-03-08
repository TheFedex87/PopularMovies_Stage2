package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import com.udacity.popularmovies1.popularmoviesstage2.MoviesAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 08/03/2018.
 */

@Singleton
@Component(modules = { UserInterfaceModule.class, ApplicationModule.class })
public interface UserInterfaceComponent {
    MoviesAdapter getMovieAdapter();
    //void inject(MoviesAdapter.ListItemClickListener listItemClickListener);
}
