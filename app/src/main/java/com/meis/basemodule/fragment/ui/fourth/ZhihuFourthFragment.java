package com.meis.basemodule.fragment.ui.fourth;

import android.os.Bundle;

import com.meis.base.mei.status.ViewState;
import com.meis.basemodule.R;
import com.meis.basemodule.base.BaseFragment;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuFourthFragment extends BaseFragment {

    public static ZhihuFourthFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFourthFragment fragment = new ZhihuFourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.user_center));

        setState(ViewState.EMPTY);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
