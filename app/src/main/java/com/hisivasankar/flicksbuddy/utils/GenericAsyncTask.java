package com.hisivasankar.flicksbuddy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hisivasankar.flicksbuddy.BuildConfig;
import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.interfaces.ITaskCompleted;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.model.Reviews;
import com.hisivasankar.flicksbuddy.model.Trailers;
import com.hisivasankar.flicksbuddy.network.HTTPServiceHandler;

import java.io.IOException;

/**
 * Created by I308944 on 2/18/2016.
 */
public class GenericAsyncTask extends AsyncTask<String, Void, Object> {
    public static final String LOG_TAG = GenericAsyncTask.class.getSimpleName();
    private final static Gson sGson = new GsonBuilder().create();
    private Context mContext;
    private SharedPreferences mPreferences;
    private Uri.Builder mUriBuilder;
    private int mRequestType;
    private Fragment mFragment;
    private Flicks.Flick mFlick;

    public GenericAsyncTask(Context context, int requestType, Fragment fragment, Flicks.Flick flick) {
        this.mContext = context;
        this.mRequestType = requestType;
        this.mFragment = fragment;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mFlick = flick;
        if (!(fragment instanceof ITaskCompleted)) {
            throw new IllegalArgumentException("Fragment must implement ITaskCompleted Interface");
        }
        if (flick == null) {
            Log.d(LOG_TAG, "Request Type : " + this.mRequestType);
            Log.d(LOG_TAG, "Flick : " + flick);
        }
    }

    @Override
    protected Object doInBackground(String... params) {
        HTTPServiceHandler httpHandler = new HTTPServiceHandler();
        String url = null, data = null;
        try {
            switch (mRequestType) {
                case Constants.DISCOVER_MOVIE_API_REQUEST:
                    url = getDiscoverMovieAPI();
                    Log.d(LOG_TAG, "Sending Request : " + this.mRequestType + " " + url);
                    data = httpHandler.doGet(url, LOG_TAG);
                    Flicks flicks = sGson.fromJson(data, Flicks.class);
                    return flicks;
                case Constants.MOVIES_TRAILERS_API_REQUEST:
                    url = getMovieTrailerAPI();
                    Log.d(LOG_TAG, "Sending Request : " + this.mRequestType + " " + url);
                    data = httpHandler.doGet(url, LOG_TAG);
                    Trailers trailers = sGson.fromJson(data, Trailers.class);
                    return trailers;
                case Constants.REVIEW_MOVIE_API_REQUEST:
                    url = getMovieReviewsAPI();
                    Log.d(LOG_TAG, "Sending Request : " + this.mRequestType + " " + url);
                    data = httpHandler.doGet(url, LOG_TAG);
                    Reviews reviews = sGson.fromJson(data, Reviews.class);
                    return reviews;
                default:
                    Log.d(LOG_TAG, "Invalid Request Type");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
        ((ITaskCompleted) mFragment).onTaskCompleted(response);
    }

    private String getDiscoverMovieAPI() {
        String sortBy = mPreferences.getString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_sort_by_default));
        mUriBuilder = Uri.parse(TheMovieDBAPI.DISOVER_API)
                .buildUpon()
                .appendQueryParameter(TheMovieDBAPI.PARAM_API_KEY, BuildConfig.THEMOVIEDBAPI_KEY)
                .appendQueryParameter(TheMovieDBAPI.PARAM_SORT_BY, sortBy);
        return mUriBuilder.toString();
    }

    private String getMovieTrailerAPI() {
        mUriBuilder = Uri.parse(TheMovieDBAPI.MOVIE_API)
                .buildUpon()
                .appendPath(mFlick.getId().toString())
                .appendPath(TheMovieDBAPI.PATH_VIDEOS)
                .appendQueryParameter(TheMovieDBAPI.PARAM_API_KEY, BuildConfig.THEMOVIEDBAPI_KEY);
        return mUriBuilder.toString();
    }

    private String getMovieReviewsAPI() {
        mUriBuilder = Uri.parse(TheMovieDBAPI.MOVIE_API)
                .buildUpon()
                .appendPath(mFlick.getId().toString())
                .appendPath(TheMovieDBAPI.PATH_REVIEWS)
                .appendQueryParameter(TheMovieDBAPI.PARAM_API_KEY, BuildConfig.THEMOVIEDBAPI_KEY);
        return mUriBuilder.toString();
    }
}
