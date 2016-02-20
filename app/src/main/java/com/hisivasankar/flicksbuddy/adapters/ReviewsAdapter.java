package com.hisivasankar.flicksbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.model.Reviews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I308944 on 2/18/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
    public static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();
    private final static String REVIEW_AUTHOR = "author";
    private final static String REVIEW_CONTENT = "content";
    private List<Reviews.Comment> mReviews;
    private Context mContext;

    public ReviewsAdapter(Context context) {
        super();
        this.mContext = context;
        mReviews = new ArrayList<>();
    }

    public void setDataSource(List<Reviews.Comment> reviews) {
        mReviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Reviews.Comment comment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextViewAuthor.setText(comment.getAuthor());
        viewHolder.mTextViewContent.setText(comment.getContent());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Reviews.Comment getItem(int position) {
        return mReviews.get(position);
    }

    class ViewHolder {
        private TextView mTextViewAuthor, mTextViewContent;

        public ViewHolder(View view) {
            mTextViewAuthor = (TextView) view.findViewById(R.id.text_review_author);
            mTextViewContent = (TextView) view.findViewById(R.id.text_review_content);
        }
    }
}