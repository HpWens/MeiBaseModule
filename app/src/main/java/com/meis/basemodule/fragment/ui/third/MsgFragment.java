package com.meis.basemodule.fragment.ui.third;

import android.os.Bundle;

import com.meis.base.mei.fragment.BaseFragment;
import com.meis.basemodule.entity.Chat;

/**
 * author: ws4
 * created on: 2018/4/13 11:18
 * description:
 */
public class MsgFragment extends BaseFragment {

    private static final String ARG_MSG = "arg_msg";

    public static MsgFragment newInstance(Chat msg) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        MsgFragment fragment = new MsgFragment();
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
