package com.hisivasankar.flicksbuddy.utils;

/**
 * Created by Sivasankar on 12/25/2015.
 */
public class TheMovieDBAPI {
    public final static String DISOVER_API = "http://api.themoviedb.org/3/discover/movie";
    public final static String CDN_IMAGE_BASEURL = "http://image.tmdb.org/t/p/w185";

    public final static String PARAM_API_KEY = "api_key";
    public final static String PARAM_SORT_BY = "sort_by";

    public final static String VALUE_POPULARITY_DESC = "popularity.desc";

    public final static String KEY_RESULTS = "results";

    /* Movie specific keys in TheMovieDB Response */

    public final static String KEY_TITLE = "title";
    public final static String KEY_POSTER_PATH = "poster_path";
    public final static String KEY_VOTE_AVERAGE = "vote_average";
    public final static String KEY_OVERVIEW = "overview";
    public final static String KEY_RELEASE_DATE = "release_date";

    public static String getPosterPath(String url) {
        return new StringBuffer().append(CDN_IMAGE_BASEURL).append(url).toString();
    }

}
