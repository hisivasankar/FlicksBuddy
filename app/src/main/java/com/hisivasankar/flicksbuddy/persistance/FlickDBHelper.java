package com.hisivasankar.flicksbuddy.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;

/**
 * Created by I308944 on 2/18/2016.
 */
public class FlickDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = FlickDBHelper.class.getSimpleName();

    private final static String DATABASE_NAME = "flickbuddy.db";
    private final static int DATABASE_VERSION = 1;

    public FlickDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVOURITE_TABLE = createFavouriteTable();
        db.execSQL(CREATE_FAVOURITE_TABLE);
        Log.d(LOG_TAG, CREATE_FAVOURITE_TABLE);
    }

    private String createFavouriteTable() {
        String createFavouriteTable = "CREATE TABLE " + FlickContract.FavouriteEntry.TABLE_NAME + " (" +
                FlickContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                FlickContract.FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FlickContract.FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FlickContract.FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FlickContract.FavouriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +

                FlickContract.FavouriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FlickContract.FavouriteEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL" +
                ");";

        return createFavouriteTable;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FlickContract.FavouriteEntry.TABLE_NAME);
            onCreate(db);
        }
    }
}
