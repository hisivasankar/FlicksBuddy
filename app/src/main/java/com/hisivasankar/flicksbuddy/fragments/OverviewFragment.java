package com.hisivasankar.flicksbuddy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.Constants;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    public static final String LOG_TAG = OverviewFragment.class.getSimpleName();

    private ImageView mFlickPosterImage;
    private TextView mFlickReleaseDate, mFlickRating, mFlickOverview;

    private Flicks.Flick mFlickDetails;


    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(Flicks.Flick flickDetails) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_FLICK_DETAILS, flickDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFlickDetails = getArguments().getParcelable(Constants.BUNDLE_FLICK_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        mFlickPosterImage = (ImageView) rootView.findViewById(R.id.image_flick_poster);
        mFlickReleaseDate = (TextView) rootView.findViewById(R.id.text_release_date);
        mFlickRating = (TextView) rootView.findViewById(R.id.text_rating);
        mFlickOverview = (TextView) rootView.findViewById(R.id.text_flick_overview);

        if (savedInstanceState == null) {
            updateFlickDetails();
        }

        return rootView;

    }

    private void updateFlickDetails() {
        if (mFlickDetails == null) {
            Log.d(LOG_TAG, "Flick Details is null.");
            return;
        }
        try {
            getActivity().setTitle(mFlickDetails.getTitle());
            mFlickRating.setText(mFlickDetails.getVoteAverage().toString());
            mFlickOverview.setText(mFlickDetails.getOverview());

            DateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toDateFormat = new SimpleDateFormat("MMM d, yyyy");
            Date releaseDate = fromDateFormat.parse(mFlickDetails.getReleaseDate());

            mFlickReleaseDate.setText(toDateFormat.format(releaseDate));

            Picasso.with(getContext()).load(TheMovieDBAPI.getPosterPath(mFlickDetails.getPosterPath()))
                    .resize(200, 250).centerInside().into(mFlickPosterImage);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_FLICK_DETAILS, mFlickDetails);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_FLICK_DETAILS)) {
            mFlickDetails = savedInstanceState.getParcelable(Constants.BUNDLE_FLICK_DETAILS);
            updateFlickDetails();
        }
    }

}
