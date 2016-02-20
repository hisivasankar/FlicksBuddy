package com.hisivasankar.flicksbuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.model.Trailers;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I308944 on 2/19/2016.
 */
public class TrailersAdapter extends BaseAdapter {
    public static final String LOG_TAG = TrailersAdapter.class.getSimpleName();

    private Context mContext;

    private List<Trailers.Trailer> mTrailers;

    public TrailersAdapter(Context context) {
        super();
        mContext = context;
        mTrailers = new ArrayList<>();
    }

    public void setDataSource(List<Trailers.Trailer> trailers) {
        mTrailers = trailers;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Trailers.Trailer trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mButtonOpenTrailer.setText("Trailer " + (position + 1));
        viewHolder.mButtonOpenTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Position : " + position);
                onItemClicked(position);
            }
        });
        return convertView;
    }

    @Override
    public Trailers.Trailer getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    public void onItemClicked(int position) {
        Trailers.Trailer trailer = this.getItem(position);
        String url = TheMovieDBAPI.getYoutubeVideoLink(trailer.getKey());
        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        videoIntent.setData(Uri.parse(url));
        if (videoIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(videoIntent);
        } else {
            Log.d(LOG_TAG, "No activity found to launch video link");
        }
    }

    static class ViewHolder {
        public Button mButtonOpenTrailer;

        public ViewHolder(View view) {
            mButtonOpenTrailer = (Button) view.findViewById(R.id.button_open_trailer);
        }
    }
}
