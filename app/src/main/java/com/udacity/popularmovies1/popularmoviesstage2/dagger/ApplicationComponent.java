package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import dagger.Component;

/**
 * Created by federico.creti on 06/03/2018.
 */
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(Context context);
}
