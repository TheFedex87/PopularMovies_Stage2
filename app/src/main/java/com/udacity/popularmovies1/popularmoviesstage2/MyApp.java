package com.udacity.popularmovies1.popularmoviesstage2;

import android.app.Application;

import com.udacity.popularmovies1.popularmoviesstage2.dagger.ApplicationComponent;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.ApplicationModule;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.Provides;

/**
 * Created by federico.creti on 06/03/2018.
 */

public class MyApp extends Application {
    private static MyApp app;
    private static ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public static MyApp app() {
        return app;
    }

    public static ApplicationComponent appComponent(){
        return appComponent;
    }
}
