package com.meis.basemodule.fragment.ui.third;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meis.base.mei.ViewState;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.fragment.BaseFragment;
import com.meis.base.mei.fragment.BaseListFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.ChatAdapter;
import com.meis.basemodule.entity.Chat;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuThirdFragment extends BaseListFragment<Chat> {

    public static ZhihuThirdFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuThirdFragment fragment = new ZhihuThirdFragment();
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
    protected RecyclerView getRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        return recyclerView;
    }

    @Override
    protected BaseAdapter<Chat> getAdapter() {
        return new ChatAdapter();
    }

    @Override
    protected Observable<Result<List<Chat>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    protected boolean canLoadMore() {
        return false;
    }

    @Override
    protected boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }
}
