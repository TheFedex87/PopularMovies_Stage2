package com.udacity.popularmovies1.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by federico.creti on 26/02/2018.
 */

public class MoviesContract {

    //URI AUTHORITY
    public static final String AUTHORITY = "com.udacity.popularmovies1.popularmoviesstage2";

    //Base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Movies path
    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // TABLE_NAME -> preferMovies;
        public static final String TABLE_NAME = "preferMovies";
        // COLUMN_ID_MOVIE -> idMovie
        public static final String COLUMN_ID_MOVIE = "idMovie";
        // COLUMN_TITLE -> title
        public static final String COLUMN_TITLE = "title";
        // COLUMN_POSTER_MOVIE -> poster
        public static final String COLUMN_POSTER = "poster";
        // COLUMN_SYNOPSIS_MOVIE -> synopsis
        public static final String COLUMN_SYNOPSIS = "synopsis";
        // COLUMNS_USER_RATING -> userRating
        public static final String COLUMN_USER_RATING = "userRating";
        // COLUMNS_RELEASE_DATE -> releaseDate
        public static final String COLUMNS_RELEASE_DATE = "releaseDate";
        // COLUMN_BACKDROP_PATH -> backdropPath
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        // COLUMN_DURATION -> duration
        public static final String COLUMN_DURATION = "duration";

    }
}
