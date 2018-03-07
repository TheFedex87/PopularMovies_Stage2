package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 05/03/2018.
 */
@Singleton
@Component(modules = { ApplicationModule.class, NetworkModule.class})
public interface RetrofitApiInterfaceComponent {
    RetrofitApiInterface getRetrofitApiInterface();
    Picasso getPicasso();
}
