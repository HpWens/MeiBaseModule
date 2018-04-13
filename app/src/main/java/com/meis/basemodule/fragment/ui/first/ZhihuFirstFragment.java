package com.meis.basemodule.fragment.ui.first;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.fragment.BaseFragment;
import com.meis.base.mei.fragment.BaseListFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.FirstHomeAdapter;
import com.meis.basemodule.adapter.MeiSimpleAdapter;
import com.meis.basemodule.entity.Article;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class ZhihuFirstFragment extends BaseFragment {

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
