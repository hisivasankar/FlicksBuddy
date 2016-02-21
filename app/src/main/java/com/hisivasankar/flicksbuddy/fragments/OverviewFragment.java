package com.hisivasankar.flicksbuddy.fragments;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.Constants;
import com.hisivasankar.flicksbuddy.utils.Helper;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OverviewFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = OverviewFragment.class.getSimpleName();

    private Context mContext;

    private ImageView mFlickPosterImage;
    private TextView mFlickReleaseDate, mFlickOverview;
    private RatingBar mRatingFlick;
    private ImageButton mImageButtonSetAsFavourite;

    private Flicks.Flick mFlickDetails;

    private View mRootView;

    private ContentResolver mContentResolver;


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
        mContext = getActivity().getApplicationContext();
        mContentResolver = mContext.getContentResolver();
        if (getArguments() != null) {
            mFlickDetails = getArguments().getParcelable(Constants.BUNDLE_FLICK_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_overview, container, false);

        mFlickPosterImage = (ImageView) mRootView.findViewById(R.id.image_flick_poster);
        mFlickReleaseDate = (TextView) mRootView.findViewById(R.id.text_release_date);
        mRatingFlick = (RatingBar) mRootView.findViewById(R.id.rating_flick_votes);
        mFlickOverview = (TextView) mRootView.findViewById(R.id.text_flick_overview);

        mImageButtonSetAsFavourite = (ImageButton) mRootView.findViewById(R.id.image_button_set_as_favourite);
        mImageButtonSetAsFavourite.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.selector));
        mImageButtonSetAsFavourite.setEnabled(false);
        mImageButtonSetAsFavourite.setOnClickListener(this);

        if (savedInstanceState == null) {
            updateFlickDetails();
        }

        return mRootView;

    }

    private void updateFlickDetails() {
        if (mFlickDetails == null) {
            Log.d(LOG_TAG, "Flick Details is null.");
            return;
        }
        try {
            getActivity().setTitle(mFlickDetails.getTitle());

            mRatingFlick.setRating(mFlickDetails.getVoteAverage().floatValue());
            mFlickOverview.setText(mFlickDetails.getOverview());

            DateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toDateFormat = new SimpleDateFormat("MMM d, yyyy");
            Date releaseDate = fromDateFormat.parse(mFlickDetails.getReleaseDate());

            mFlickReleaseDate.setText(toDateFormat.format(releaseDate));

            Picasso.with(getContext()).load(TheMovieDBAPI.getPosterPath(mFlickDetails.getPosterPath()))
                    .resize(200, 250).centerInside().into(mFlickPosterImage);
            mImageButtonSetAsFavourite.setEnabled(true);
            mImageButtonSetAsFavourite.setSelected(isFavouriteFlick());


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

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.image_button_set_as_favourite) {
            if (mImageButtonSetAsFavourite.isSelected()) {
                mImageButtonSetAsFavourite.setSelected(false);
                deleteFromFavourite();
            } else {
                mImageButtonSetAsFavourite.setSelected(true);
                setAsFavourite();
            }
        }
    }

    private boolean isFavouriteFlick() {
        Uri uri = FlickContract.FavouriteEntry.buildFavouriteUri(mFlickDetails.getId());
        String[] projection = new String[]{
                FlickContract.FavouriteEntry.COLUMN_MOVIE_ID
        };
        String selection = FlickContract.FavouriteEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = new String[]{
                mFlickDetails.getId().toString()
        };

        Cursor cursor = mContentResolver.query(uri, projection, selection, selectionArgs, null);
        boolean isFavourite = false;
        if (cursor.moveToFirst()) {
            isFavourite = cursor.moveToFirst();
        }
        cursor.close();
        return isFavourite;
    }

    private void setAsFavourite() {
        Uri favouriteUri = FlickContract.FavouriteEntry.buildFavouriteUri(mFlickDetails.getId());
        ContentValues flickContentValues = Helper.getContentValues(mFlickDetails);
        Uri uri = mContentResolver.insert(favouriteUri, flickContentValues);
        if (uri != null) {
            Snackbar.make(mRootView, "Added to Favourites", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mRootView, "Failed to add to Favourites", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void deleteFromFavourite() {
        Uri uri = FlickContract.FavouriteEntry.buildFavouriteUri(mFlickDetails.getId());
        String selection = FlickContract.FavouriteEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = new String[]{
                mFlickDetails.getId().toString()
        };
        int numRowsDeleted = mContentResolver.delete(uri, selection, selectionArgs);
        if (numRowsDeleted > 0) {
            Snackbar.make(mRootView, "Removed from Favourites", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mRootView, "Failed to remove from Favourites", Snackbar.LENGTH_SHORT).show();
        }
    }
}
