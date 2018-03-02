package com.udacity.popularmovies1.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by federico.creti on 27/02/2018.
 */

public class MovieContentProvider extends ContentProvider {
    private MovieDbHelper movieDbHelper;

    //Constants of URI Matcher
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher uriMatcher = builUriMatcher();

    public static UriMatcher builUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //UriMatcher for all movies
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);

        //UriMatcher for a specific movie
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        int match = MovieContentProvider.uriMatcher.match(uri);

        Cursor retCursor;

        switch(match){
            case(MOVIES):
                retCursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case(MOVIES_WITH_ID):

                String id = uri.getPathSegments().get(1);

                String mSelection = "idMovie=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = MovieContentProvider.uriMatcher.match(uri);

        Uri returnUri;

        switch(match){
            case MOVIES:
                long id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                }
                else{
                    throw new SQLException("Failed to insert data into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = MovieContentProvider.uriMatcher.match(uri);

        int deletedRows = 0;

        switch (match){
            case MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String where = "_id=?";
                String[] whereArgs = new String[]{id};

                deletedRows = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, where, whereArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unkonown URI: " + uri);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
