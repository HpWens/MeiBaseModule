package com.meis.basemodule.fragment.ui.second;

import android.os.Bundle;
import android.widget.TextView;

import com.meis.base.mei.fragment.BaseFragment;
import com.meis.basemodule.R;


/**
 * Created by YoKeyword on 16/6/30.
 */
public class OtherPagerFragment extends BaseFragment {

    private static final String ARG_MSG = "arg_msg";

    private String mTitle = "";

    public static OtherPagerFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(ARG_MSG, title);
        OtherPagerFragment fragment = new OtherPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_MSG)) {
                this.mTitle = args.getString(ARG_MSG);
            }
        }
        ((TextView) findViewById(R.id.tv_title)).setText(mTitle);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_tab_second_pager;
    }
}
