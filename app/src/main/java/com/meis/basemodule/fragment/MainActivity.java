package com.meis.basemodule.fragment;

import android.support.v4.app.ActivityCompat;
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

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private BaseFragment[] mFragments = new BaseFragment[4];
    private BottomBar mBottomBar;

    @Override
    protected void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_home_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_discover_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_message_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_account_circle_white_24dp));

    }

    @Override
    protected void initData() {

        BaseFragment firstFragment = findFragment(ZhihuFirstFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = ZhihuFirstFragment.newInstance();
            mFragments[SECOND] = ZhihuSecondFragment.newInstance();
            mFragments[THIRD] = ZhihuThirdFragment.newInstance();
            mFragments[FOURTH] = ZhihuFourthFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(ZhihuSecondFragment.class);
            mFragments[THIRD] = findFragment(ZhihuThirdFragment.class);
            mFragments[FOURTH] = findFragment(ZhihuFourthFragment.class);
        }

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_fragment;
    }
}
