package com.hisivasankar.flicksbuddy.contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by I308944 on 2/20/2016.
 */
public class FlickContract {
    public final static String CONTENT_AUTHORITY = "com.hisivasankar.flicksbuddy.app";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public final static String PATH_FAVOURITES = "favourites";

    public static class FavouriteEntry implements BaseColumns {
        public final static String TABLE_NAME = "flick_favourites";

        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public final static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public final static String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        public final static String COLUMN_MOVIE_ID = "movie_id";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_OVERVIEW = "overview";
        public final static String COLUMN_VOTE_AVERAGE = "vote_average";
        public final static String COLUMN_POSTER_PATH = "poster_path";
        public final static String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildFavouriteUri(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movieId)).build();
        }

    }
}
