package com.meis.basemodule.fragment.ui.second;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.meis.base.mei.fragment.MeiBaseFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.ZhihuPagerFragmentAdapter;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuSecondFragment extends MeiBaseFragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    public static ZhihuSecondFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuSecondFragment fragment = new ZhihuSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.pager);
    }

    @Override
    protected void initData() {
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        mViewPager.setAdapter(new ZhihuPagerFragmentAdapter(getChildFragmentManager(),
                getString(R.string.recommend), getString(R.string.hot), getString(R.string.favorite),
                getString(R.string.more)));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_second;
    }
}
