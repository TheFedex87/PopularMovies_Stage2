package com.udacity.popularmovies1.popularmoviesstage2.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies1.popularmoviesstage2.retrofit.RetrofitApiInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by federico.creti on 05/03/2018.
 */
@Module(includes = ContextModule.class)
public class NetworkModule {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    @Singleton
    @Provides
    public RetrofitApiInterface provideRetrofiApiInterface(Retrofit retrofit){
        return retrofit.create(RetrofitApiInterface.class);
    }

    @Singleton
    @Provides
    public Gson provideGson(){
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    public Picasso providePicasso(Context context){
        return Picasso.with(context);
    }
}
