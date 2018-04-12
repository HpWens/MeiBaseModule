package com.meis.basemodule.fragment.ui.second;

import android.os.Bundle;

import com.meis.base.mei.fragment.BaseFragment;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuSecondFragment extends BaseFragment {

    public static ZhihuSecondFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuSecondFragment fragment = new ZhihuSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
