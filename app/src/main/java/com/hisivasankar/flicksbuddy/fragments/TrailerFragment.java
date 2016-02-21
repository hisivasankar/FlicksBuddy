package com.hisivasankar.flicksbuddy.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.adapters.TrailersAdapter;
import com.hisivasankar.flicksbuddy.interfaces.ITaskCompleted;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.model.Trailers;
import com.hisivasankar.flicksbuddy.utils.Constants;
import com.hisivasankar.flicksbuddy.utils.GenericAsyncTask;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrailerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrailerFragment extends Fragment implements ITaskCompleted {
    public static final String LOG_TAG = TrailerFragment.class.getSimpleName();

    private Context mContext;

    private Flicks.Flick mFlickDetails;

    private TrailersAdapter mTrailerAdapter;
    private Trailers mTrailers;
    private ListView mListViewTrailers;

    private ShareActionProvider mShareActionProvider = null;

    public TrailerFragment() {
        // Required empty public constructor
    }

    public static TrailerFragment newInstance(Flicks.Flick flickDetails) {
        TrailerFragment fragment = new TrailerFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_FLICK_DETAILS, flickDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mFlickDetails = getArguments().getParcelable(Constants.BUNDLE_FLICK_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();

        View rootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        mListViewTrailers = (ListView) rootView.findViewById(R.id.list_view_trailers);
        mListViewTrailers.setEmptyView(rootView.findViewById(android.R.id.empty));

        mTrailerAdapter = new TrailersAdapter(mContext);
        mListViewTrailers.setAdapter(mTrailerAdapter);

        if (savedInstanceState == null) {
            GenericAsyncTask mTrailerTask = new GenericAsyncTask(mContext, Constants.MOVIES_TRAILERS_API_REQUEST, this, mFlickDetails);
            mTrailerTask.execute();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_TRAILERS, mTrailers);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_TRAILERS)) {
            mTrailers = savedInstanceState.getParcelable(Constants.BUNDLE_TRAILERS);
            mTrailerAdapter.setDataSource(mTrailers.getResults());
            mTrailerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskCompleted(Object object) {
        if (object instanceof Trailers) {
            mTrailers = (Trailers) object;
            mTrailerAdapter.setDataSource(mTrailers.getResults());
            mTrailerAdapter.notifyDataSetChanged();
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(getTrailerShareIntent());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(getTrailerShareIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Intent getTrailerShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String shareURL = "Sorry! No Trailers found";
        if (mTrailerAdapter.getCount() > 0) {
            shareURL = TheMovieDBAPI.getYoutubeVideoLink(mTrailerAdapter.getItem(0).getKey());
        }
        share.putExtra(Intent.EXTRA_TEXT, shareURL);
        return share;
    }
}
