package com.hisivasankar.flicksbuddy.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.activities.FlickListingActivity;
import com.hisivasankar.flicksbuddy.activities.SettingsActivity;
import com.hisivasankar.flicksbuddy.adapters.FlicksAdapter;
import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.interfaces.IFlickSelected;
import com.hisivasankar.flicksbuddy.interfaces.ITaskCompleted;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.Constants;
import com.hisivasankar.flicksbuddy.utils.GenericAsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class FlickListingFragment extends Fragment implements ITaskCompleted {
    public static final String LOG_TAG = FlickListingFragment.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private FlicksAdapter mFlickAdapter;
    private GridView mFlicksGridView;
    private Flicks mFlicksDataSource;

    private GenericAsyncTask mFlickFetcherTask;

    private String mSortBy;
    private IFlickSelected mListener;
    private boolean mIsShowingFavourite;

    public FlickListingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSortBy = mSharedPreferences.getString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_sort_by_default));
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_SORT_BY)) {
            mSortBy = savedInstanceState.getString(Constants.BUNDLE_SORT_BY);
            mIsShowingFavourite = savedInstanceState.getBoolean(Constants.BUNDLE_IS_SHOWING_FAVOURITE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_FLICKS, mFlicksDataSource);
        outState.putString(Constants.BUNDLE_SORT_BY, mSortBy);
        outState.putBoolean(Constants.BUNDLE_IS_SHOWING_FAVOURITE, mIsShowingFavourite);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_FLICKS)) {
            mFlicksDataSource = savedInstanceState.getParcelable(Constants.BUNDLE_FLICKS);
            mFlickAdapter.setDataSource(mFlicksDataSource.getResults());
            mFlickAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        if (savedInstanceState == null) {
            mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.DISCOVER_MOVIE_API_REQUEST, this, null);
            mFlickFetcherTask.execute();
        }

        mFlickAdapter = new FlicksAdapter(mContext);
        mFlicksGridView = (GridView) rootView.findViewById(R.id.grid_flicks);
        mFlicksGridView.setEmptyView(rootView.findViewById(android.R.id.empty));
        mFlicksGridView.setAdapter(mFlickAdapter);
        mFlicksGridView.setScrollingCacheEnabled(true);
        mFlicksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Flicks.Flick flick = mFlickAdapter.getItem(position);
                mListener.onFlickSelected(flick);
            }
        });
        return rootView;
    }

    public String getSortBySettings() {
        return mSharedPreferences.getString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_sort_by_default));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mSortBy.equals(getSortBySettings())) {
            mIsShowingFavourite = false;
            mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.DISCOVER_MOVIE_API_REQUEST, this, null);
            mFlickFetcherTask.execute();
            Log.d(LOG_TAG, "Reload needed");
        } else if (mIsShowingFavourite) {
            showFavouriteFlicks();
        }
    }

    @Override
    public void onTaskCompleted(Object object) {
        if (object instanceof Flicks) {
            mFlicksDataSource = (Flicks) object;
            mFlickAdapter.setDataSource(mFlicksDataSource.getResults());
            mFlickAdapter.notifyDataSetChanged();
            if (mFlickAdapter.getCount() > 0 && ((FlickListingActivity) mListener).mIsTwoPaneLayout) {
                mListener.onFlickSelected(mFlickAdapter.getItem(0));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFlickSelected) {
            mListener = (IFlickSelected) context;
        } else {
            throw new RuntimeException("Hosting activity must implement IFlickSelected Interface");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(mContext, SettingsActivity.class);
            startActivity(settings);
            return true;
        } else if (id == R.id.action_favourites) {
            mIsShowingFavourite = true;
            showFavouriteFlicks();
        } else if (id == R.id.action_top_rated) {
            mIsShowingFavourite = false;
            showTopRatedFlicks();
        } else if (id == R.id.action_popularity) {
            mIsShowingFavourite = false;
            showPopularFlicks();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFavouriteFlicks() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(FlickContract.FavouriteEntry.CONTENT_URI, null, null, null, null);
        mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.FAVOURITE_MOVIES_REQUEST, this, null);
        mFlickFetcherTask.execute(cursor);
    }

    private void showTopRatedFlicks() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_top_rated));
        editor.commit();
        mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.DISCOVER_MOVIE_API_REQUEST, this, null);
        mFlickFetcherTask.execute();
    }

    private void showPopularFlicks() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_popularity));
        editor.commit();
        mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.DISCOVER_MOVIE_API_REQUEST, this, null);
        mFlickFetcherTask.execute();
    }
}