package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.app.Application;
import android.content.Context;

import com.udacity.popularmovies1.popularmoviesstage2.MyApp;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 06/03/2018.
 */
@Module
public class ApplicationModule {
    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
