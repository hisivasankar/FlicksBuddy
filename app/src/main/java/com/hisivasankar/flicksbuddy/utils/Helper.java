package com.hisivasankar.flicksbuddy.utils;

import android.content.ContentValues;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.model.Flicks;

/**
 * Created by I308944 on 2/21/2016.
 */
public class Helper {
    public static ContentValues getContentValues(Flicks.Flick flick) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_MOVIE_ID, flick.getId());
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_TITLE, flick.getTitle());
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_OVERVIEW, flick.getOverview());
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_POSTER_PATH, flick.getPosterPath());
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_RELEASE_DATE, flick.getReleaseDate());
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_VOTE_AVERAGE, flick.getVoteAverage());
        return contentValues;
    }
}
