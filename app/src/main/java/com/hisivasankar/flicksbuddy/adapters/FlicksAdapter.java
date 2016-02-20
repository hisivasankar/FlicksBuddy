package com.hisivasankar.flicksbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.TheMovieDBAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I308944 on 2/18/2016.
 */
public class FlicksAdapter extends BaseAdapter {
    private Context mContext;

    private List<Flicks.Flick> mFlicks;

    public FlicksAdapter(Context context) {
        mContext = context;
        mFlicks = new ArrayList<Flicks.Flick>();
    }

    public void setDataSource(List<Flicks.Flick> object) {
        mFlicks = object;
    }

    @Override
    public int getCount() {
        return mFlicks.size();
    }

    @Override
    public Flicks.Flick getItem(int position) {
        return mFlicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String posterPath = getItem(position).getPosterPath();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_flick, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImagePoster.setAdjustViewBounds(true);
        Picasso.with(mContext).load(TheMovieDBAPI.getPosterPath(posterPath)).resize(200, 350).centerInside().into(viewHolder.mImagePoster);
        return convertView;
    }

    static class ViewHolder {
        public ImageView mImagePoster;

        public ViewHolder(View view) {
            mImagePoster = (ImageView) view.findViewById(R.id.image_flick);
        }
    }
}