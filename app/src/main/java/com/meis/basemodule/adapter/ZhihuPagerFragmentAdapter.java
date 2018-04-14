package com.meis.basemodule.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.meis.basemodule.fragment.ui.second.OtherPagerFragment;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class ZhihuPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public ZhihuPagerFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return OtherPagerFragment.newInstance(mTitles[position]);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
