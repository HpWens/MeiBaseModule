package com.meis.basemodule.fragment;

import com.meis.basemodule.R;
import com.meis.basemodule.base.BaseActivity;

/**
 * author: ws4
 * created on: 2018/4/11 15:06
 * description:
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
        enableKeyboardVisibilityListener();
    }

    @Override
    protected int layoutResId() {
        return R.layout.zhihu_activity_main;
    }

}
