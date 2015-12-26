package com.hisivasankar.flicksbuddy.layouts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class FlickDetailsActivityFragment extends Fragment {
    private ImageView mFlickPosterImage;
    private TextView mFlickReleaseDate, mFlickRating, mFlickOverview;

    public FlickDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flick_details, container, false);

        mFlickPosterImage = (ImageView) rootView.findViewById(R.id.image_flick_poster);
        mFlickReleaseDate = (TextView) rootView.findViewById(R.id.text_release_date);
        mFlickRating = (TextView) rootView.findViewById(R.id.text_rating);
        mFlickOverview = (TextView) rootView.findViewById(R.id.text_flick_overview);

        Intent flickIntent = getActivity().getIntent();
        if (flickIntent != null) {
            String data = flickIntent.getStringExtra(Intent.EXTRA_TEXT);
            try {
                JSONObject flickDetails = new JSONObject(data);
                getActivity().setTitle(flickDetails.getString(TheMovieDBAPI.KEY_TITLE));
                mFlickOverview.setText(flickDetails.getString(TheMovieDBAPI.KEY_OVERVIEW));
                mFlickRating.setText(String.valueOf(flickDetails.getDouble(TheMovieDBAPI.KEY_VOTE_AVERAGE)) + "/10");
                DateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-d");
                SimpleDateFormat toDateFormat = new SimpleDateFormat("MMM d, yyyy");
                Date releaseDate = fromDateFormat.parse(flickDetails.getString(TheMovieDBAPI.KEY_RELEASE_DATE));
                mFlickReleaseDate.setText(toDateFormat.format(releaseDate));
                Picasso.with(getContext()).load(TheMovieDBAPI.getPosterPath(flickDetails.getString(TheMovieDBAPI.KEY_POSTER_PATH)))
                        .resize(200, 250).centerInside().into(mFlickPosterImage);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return rootView;
    }

    private class UpdateFlickDetails extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... data) {
            return null;
        }
    }
}
