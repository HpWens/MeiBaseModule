package com.meis.basemodule.fragment.ui.first;

import android.os.Bundle;

import com.meis.base.mei.fragment.MeiBaseFragment;
import com.meis.basemodule.R;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class ZhihuFirstFragment extends MeiBaseFragment {

    public static ZhihuFirstFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFirstFragment fragment = new ZhihuFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (findChildFragment(FirstHomeFragment.class) == null) {
            loadRootFragment(R.id.fl_first_container, FirstHomeFragment.newInstance());
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_first;
    }


    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (this instanceof ZhihuFirstFragment) {   // 如果是 第一个Fragment 则退出app
                mActivity.finish();
            }
        }
        return true;
    }
}
