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

/**
 * Class used to store the ApplicationContext in order to be inject later throught Dagger 2
 */
public class MyApp extends Application {
    private static ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public static ApplicationComponent appComponent(){
        return appComponent;
    }
}
