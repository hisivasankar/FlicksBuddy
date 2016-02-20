package com.hisivasankar.flicksbuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.adapters.PagerAdapter;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FlickDetailsFragment extends Fragment {
    public static final String LOG_TAG = FlickDetailsFragment.class.getSimpleName();

    private Context mContext;

    private Flicks.Flick mFlickDetails;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    public FlickDetailsFragment() {

    }

    public static FlickDetailsFragment newInstance(Flicks.Flick flickDetails) {
        FlickDetailsFragment flickDetailsFragment = new FlickDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_FLICK_DETAILS, flickDetails);
        flickDetailsFragment.setArguments(args);
        return flickDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent flickDetailsIntent = getActivity().getIntent();

        if (getArguments() != null) {
            mFlickDetails = getArguments().getParcelable(Constants.BUNDLE_FLICK_DETAILS);
        } else if (flickDetailsIntent != null && flickDetailsIntent.hasExtra(Constants.BUNDLE_FLICK_DETAILS)) {
            mFlickDetails = flickDetailsIntent.getParcelableExtra(Constants.BUNDLE_FLICK_DETAILS);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_FLICK_DETAILS)) {
            mFlickDetails = savedInstanceState.getParcelable(Constants.BUNDLE_FLICK_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flick_details, container, false);
        mContext = getActivity().getApplicationContext();

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager_flick_holder);
        if (mFlickDetails != null) {
            mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getFragments());
            mViewPager.setAdapter(mPagerAdapter);
        }
        return rootView;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();

        OverviewFragment overviewFragment = OverviewFragment.newInstance(mFlickDetails);
        ReviewFragment reviewFragment = ReviewFragment.newInstance(mFlickDetails);
        TrailerFragment trailerFragment = TrailerFragment.newInstance(mFlickDetails);

        fragments.add(overviewFragment);
        fragments.add(reviewFragment);
        fragments.add(trailerFragment);

        return fragments;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_FLICK_DETAILS, mFlickDetails);
        super.onSaveInstanceState(outState);
    }
}
