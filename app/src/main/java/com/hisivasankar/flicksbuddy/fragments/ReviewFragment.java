package com.hisivasankar.flicksbuddy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.adapters.ReviewsAdapter;
import com.hisivasankar.flicksbuddy.interfaces.ITaskCompleted;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.model.Reviews;
import com.hisivasankar.flicksbuddy.utils.Constants;
import com.hisivasankar.flicksbuddy.utils.GenericAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment implements ITaskCompleted {

    private ListView mListViewReviews;

    private ReviewsAdapter mReviewAdapter;
    private Reviews mReviews;

    private Flicks.Flick mFlickDetails;

    private Context mContext;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(Flicks.Flick flickDetails) {
        ReviewFragment fragment = new ReviewFragment();
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

        mContext = getActivity().getApplicationContext();

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mListViewReviews = (ListView) rootView.findViewById(R.id.list_view_reviews);
        mListViewReviews.setEmptyView(rootView.findViewById(android.R.id.empty));

        mReviewAdapter = new ReviewsAdapter(mContext);
        mListViewReviews.setAdapter(mReviewAdapter);

        if (savedInstanceState == null) {
            GenericAsyncTask mReviewsTask = new GenericAsyncTask(mContext, Constants.REVIEW_MOVIE_API_REQUEST, this, mFlickDetails);
            mReviewsTask.execute();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_REVIEWS, mReviews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.BUNDLE_REVIEWS)) {
            mReviews = savedInstanceState.getParcelable(Constants.BUNDLE_REVIEWS);
            mReviewAdapter.setDataSource(mReviews.getResults());
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskCompleted(Object object) {
        if (object instanceof Reviews) {
            mReviews = (Reviews) object;
            mReviewAdapter.setDataSource(mReviews.getResults());
            mReviewAdapter.notifyDataSetChanged();
        }
    }

}
