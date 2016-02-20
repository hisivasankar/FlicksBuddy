package com.hisivasankar.flicksbuddy.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.activities.FlickListingActivity;
import com.hisivasankar.flicksbuddy.adapters.FlicksAdapter;
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

    public FlickListingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSortBy = mSharedPreferences.getString(mContext.getString(R.string.preference_sort_by), mContext.getString(R.string.preference_sort_by_default));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_SORT_BY)) {
            mSortBy = savedInstanceState.getString(Constants.BUNDLE_SORT_BY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_FLICKS, mFlicksDataSource);
        outState.putString(Constants.BUNDLE_SORT_BY, mSortBy);
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
            mFlickFetcherTask = new GenericAsyncTask(mContext, Constants.DISCOVER_MOVIE_API_REQUEST, this, null);
            mFlickFetcherTask.execute();
            Log.d(LOG_TAG, "Reload needed");
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
}