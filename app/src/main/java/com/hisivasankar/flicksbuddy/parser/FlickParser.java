package com.hisivasankar.flicksbuddy.parser;

import android.database.Cursor;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.model.Flicks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I308944 on 2/20/2016.
 */
public class FlickParser {

    public Flicks parserFlicks(Cursor cursor) {

        Flicks flicks = new Flicks();
        List<Flicks.Flick> flickList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Flicks.Flick flick = new Flicks.Flick();

            int movieIDIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_MOVIE_ID);
            int movieTitleIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_TITLE);
            int movieOverviewIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_OVERVIEW);
            int moviePosterIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_POSTER_PATH);
            int movieVoteAverageIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_VOTE_AVERAGE);
            int movieReleaseDateIndex = cursor.getColumnIndex(FlickContract.FavouriteEntry.COLUMN_RELEASE_DATE);

            flick.setId(cursor.getInt(movieIDIndex));
            flick.setTitle(cursor.getString(movieTitleIndex));
            flick.setOverview(cursor.getString(movieOverviewIndex));
            flick.setPosterPath(cursor.getString(moviePosterIndex));
            flick.setVoteAverage(cursor.getDouble(movieVoteAverageIndex));
            flick.setReleaseDate(cursor.getString(movieReleaseDateIndex));

            flickList.add(flick);
        }

        return flicks;
    }
}
