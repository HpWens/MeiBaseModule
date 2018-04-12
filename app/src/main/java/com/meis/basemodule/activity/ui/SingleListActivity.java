package com.meis.basemodule.activity.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.meis.base.mei.BaseListActivity;
import com.meis.base.mei.ViewState;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.rxjava.UiSubscriber;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.MeiSimpleAdapter;
import com.meis.basemodule.entity.Article;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * author: ws4
 * created on: 2018/4/11 15:32
 * description:
 */
public class SingleListActivity extends BaseListActivity<Article> {

    RecyclerView mRecyclerView;
    MeiSimpleAdapter mAdapter;

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.simple_type));
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setState(ViewState.LOADING);
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        return mRecyclerView;
    }

    @Override
    protected BaseAdapter<Article> getAdapter() {
        return mAdapter = new MeiSimpleAdapter();
    }


    @Override
    protected Observable<Result<List<Article>>> getListObservable(int pageNo) {
        return getData(pageNo);
    }

    public Observable<Result<List<Article>>> getData(int pageNo) {
        Result<List<Article>> result = new Result<>();
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * 3);
            Article article = new Article(getResources().getStringArray(R.array.array_title)
                    [index], getResources().getStringArray(R.array.array_content)[index]);
            articleList.add(article);
        }
        result.data = articleList;
        result.resultCode = 0;
        result.message = "";
        return Observable.just(result);
    }

    @Override
    protected void loadPage(final int pageNo) {
        postUiThreads(2000, new UiSubscriber<Long>() {
            @Override
            public void onCompleted() {
                SingleListActivity.super.loadPage(pageNo);
            }
        });
    }

    @Override
    protected boolean canLoadMore() {
        return true;
    }

    @Override
    protected boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected int layoutResId() {
        return R.layout.comm_recycler;
    }
}
