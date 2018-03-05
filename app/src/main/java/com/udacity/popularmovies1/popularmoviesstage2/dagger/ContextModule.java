package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 05/03/2018.
 */
@Module
public class ContextModule {
    Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
