package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import com.udacity.popularmovies1.popularmoviesstage2.MoviesAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 08/03/2018.
 */
@Module
public class UserInterfaceModule {

    private MoviesAdapter.ListItemClickListener listItemClickListener;

    public UserInterfaceModule(MoviesAdapter.ListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }

    @Singleton
    @Provides
    public MoviesAdapter provideMoviesAdapter(){
        return new MoviesAdapter(listItemClickListener);
    }
}
