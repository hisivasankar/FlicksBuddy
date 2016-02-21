package com.hisivasankar.flicksbuddy.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.persistance.FlickDBHelper;

/**
 * Created by I308944 on 2/20/2016.
 */
public class FlickContentProvider extends ContentProvider {
    public static final String LOG_TAG = FlickContentProvider.class.getSimpleName();

    private final static int FAVOURITES = 25;
    private final static int FAVOURITE_WITH_ID = 26;

    private static UriMatcher sUriMatcher = createUriMatcher();

    private FlickDBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public FlickContentProvider() {
        super();
    }

    private static UriMatcher createUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FlickContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FlickContract.PATH_FAVOURITES, FAVOURITES);
        uriMatcher.addURI(authority, FlickContract.PATH_FAVOURITES + "/#", FAVOURITE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new FlickDBHelper(getContext());
        mDB = mDBHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVOURITES:
                return FlickContract.FavouriteEntry.CONTENT_TYPE;
            case FAVOURITE_WITH_ID:
                return FlickContract.FavouriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Type : " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);

        Log.d(LOG_TAG, "Matcher ID : " + match);
        Cursor resultCursor = null;
        switch (match) {
            case FAVOURITES:
                resultCursor = mDBHelper.getReadableDatabase().query(
                        FlickContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                if (resultCursor == null) {
                    Log.d(LOG_TAG, "Watch out bro, Cursor is null");
                } else {
                    Log.d(LOG_TAG, "Valid Cursor found");
                }
                break;
            case FAVOURITE_WITH_ID:
                resultCursor = mDBHelper.getReadableDatabase().query(
                        FlickContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Type Requested : " + uri);
        }
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return resultCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted = 0;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVOURITE_WITH_ID:
                numRowsDeleted = mDBHelper.getWritableDatabase().delete(
                        FlickContract.FavouriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown type : " + uri);
        }
        return numRowsDeleted;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Uri newFlickUri = null;
        switch (match) {
            case FAVOURITE_WITH_ID:
                long id = mDB.insert(FlickContract.FavouriteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    newFlickUri = FlickContract.FavouriteEntry.buildFavouriteUri(id);
                } else {
                    throw new SQLiteException("Falied to insert row");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Type : " + uri);
        }
        getContext().getContentResolver().notifyChange(newFlickUri, null);
        return newFlickUri;
    }
}
