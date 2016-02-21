package com.hisivasankar.flicksbuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hisivasankar.flicksbuddy.R;
import com.hisivasankar.flicksbuddy.fragments.FlickDetailsFragment;
import com.hisivasankar.flicksbuddy.interfaces.IFlickSelected;
import com.hisivasankar.flicksbuddy.model.Flicks;
import com.hisivasankar.flicksbuddy.utils.Constants;

public class FlickListingActivity extends AppCompatActivity implements IFlickSelected {
    public static final String LOG_TAG = FlickListingActivity.class.getSimpleName();

    public final static String TAG_FLICK_DETAILS_FRAG = "FLICK_DETAILS_FRAGMENT";
    public boolean mIsTwoPaneLayout = false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        if (findViewById(R.id.flick_details_container) != null) {
            mIsTwoPaneLayout = true;
        }

        Log.d(LOG_TAG, "Two pane layout : " + mIsTwoPaneLayout);
    }

    @Override
    public void onFlickSelected(Flicks.Flick flick) {
        if (mIsTwoPaneLayout) {
            FlickDetailsFragment flickDetailsFragment = FlickDetailsFragment.newInstance(flick);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flick_details_container, flickDetailsFragment, TAG_FLICK_DETAILS_FRAG);
            ft.commit();
        } else {
            Intent flickDetailsIntent = new Intent(mContext, FlickDetailsActivity.class);
            flickDetailsIntent.putExtra(Constants.BUNDLE_FLICK_DETAILS, flick);
            startActivity(flickDetailsIntent);
        }
    }
}
