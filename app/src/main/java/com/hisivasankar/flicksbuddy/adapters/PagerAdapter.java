package com.hisivasankar.flicksbuddy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by I308944 on 2/20/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private final int NUM_OF_TABS = 3;
    private final String[] mPagerTitle = new String[]{
            "Overview", "Reviews", "Trailers"
    };
    private List<Fragment> mFragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragments = fragmentList;
    }

    public void setDataSource(List<Fragment> fragmentList) {
        this.mFragments = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPagerTitle[position];
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
