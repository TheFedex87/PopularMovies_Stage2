package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import com.udacity.popularmovies1.popularmoviesstage2.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 06/03/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity context);
}
