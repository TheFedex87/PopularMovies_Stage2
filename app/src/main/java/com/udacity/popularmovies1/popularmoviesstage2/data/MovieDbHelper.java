package com.udacity.popularmovies1.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by federico.creti on 26/02/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_ID_MOVIE + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_SYNOPSIS + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_USER_RATING + " FLOAT, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_DURATION + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMNS_RELEASE_DATE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
