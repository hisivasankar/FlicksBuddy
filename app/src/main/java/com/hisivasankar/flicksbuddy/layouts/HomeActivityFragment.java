package com.hisivasankar.flicksbuddy.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hisivasankar.flicksbuddy.BuildConfig;
import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.utils.HTTPServiceHandler;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {
    public static final String LOG_TAG = HomeActivityFragment.class.getSimpleName();
    private FlicksAdaptor mFlickAdapter;
    private GridView mFlicksGridView;
    private JSONArray mMoviesData;
    private Context mContext;

    public HomeActivityFragment() {
        mMoviesData = new JSONArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getActivity().getApplicationContext();

        mFlickAdapter = new FlicksAdaptor(mContext);
        mFlicksGridView = (GridView) rootView.findViewById(R.id.grid_flicks);
        mFlicksGridView.setAdapter(mFlickAdapter);
        mFlicksGridView.setScrollingCacheEnabled(true);
        mFlicksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Snackbar.make(view, "Hello!, you clicked something", Snackbar.LENGTH_SHORT).show();
                startActivity(getFlickDetailsActivityIntent(position));
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new MoviesUpdaterTask().execute();
    }

    private Intent getFlickDetailsActivityIntent(int position) {
        Intent flickDetailsIntent = new Intent(mContext, FlickDetailsActivity.class);
        try {
            Bundle bundle = new Bundle();
            flickDetailsIntent.putExtra(Intent.EXTRA_TEXT, mMoviesData.getJSONObject(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flickDetailsIntent;
    }

    private class MoviesUpdaterTask extends AsyncTask<Void, Void, Void> {
        public MoviesUpdaterTask() {
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String sortyBy = preferences.getString(getString(R.string.preference_sort_by), getString(R.string.preference_sort_by_default));
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put(TheMovieDBAPI.PARAM_API_KEY, BuildConfig.THEMOVIEDBAPI_KEY);
            queryParams.put(TheMovieDBAPI.PARAM_SORT_BY, sortyBy);
            HTTPServiceHandler handler = new HTTPServiceHandler();
            try {
                String data = handler.doGet(LOG_TAG, TheMovieDBAPI.DISOVER_API, queryParams);
                if (data != null) {
                    JSONObject response = new JSONObject(data);
                    mMoviesData = response.getJSONArray(TheMovieDBAPI.KEY_RESULTS);
                    Log.d(LOG_TAG, mMoviesData.toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mFlickAdapter.notifyDataSetChanged();
        }

    }

    private class FlicksAdaptor extends BaseAdapter {
        private Context mContext;

        public FlicksAdaptor(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mMoviesData.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return mMoviesData.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String posterPath = "";
            try {
                posterPath = mMoviesData.getJSONObject(position).getString(TheMovieDBAPI.KEY_POSTER_PATH);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageView flickImageView;
            if (convertView == null) {
                View view = getActivity().getLayoutInflater().from(mContext).inflate(R.layout.item_flick, null);
                flickImageView = (ImageView) view.findViewById(R.id.image_flick);
                flickImageView.setAdjustViewBounds(true);
            } else {
                flickImageView = (ImageView) convertView;
            }
            Picasso.with(mContext).load(TheMovieDBAPI.getPosterPath(posterPath)).resize(200, 350).centerInside().into(flickImageView);
            return flickImageView;
        }
    }
}