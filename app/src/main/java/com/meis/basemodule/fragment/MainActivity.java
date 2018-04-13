package com.meis.basemodule.fragment;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.meis.base.mei.BaseActivity;
import com.meis.base.mei.fragment.BaseFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.fragment.ui.first.ZhihuFirstFragment;
import com.meis.basemodule.fragment.ui.fourth.ZhihuFourthFragment;
import com.meis.basemodule.fragment.ui.second.ZhihuSecondFragment;
import com.meis.basemodule.fragment.ui.third.ZhihuThirdFragment;
import com.meis.basemodule.widget.BottomBar;
import com.meis.basemodule.widget.BottomBarTab;

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
    }

    @Override
    protected int layoutResId() {
        return R.layout.wechat_activity_main;
    }
}
