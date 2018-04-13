package com.meis.base.mei.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.DataConstants;
import com.meis.base.mei.ViewState;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.ListUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * author: ws4
 * created on: 2018/4/11 14:24
 * description:
 */
public abstract class BaseListFragment<T> extends BaseFragment {

    protected BaseAdapter<T> mAdapter;

    private int mPageNo;

    @Override
    protected void initView() {
        mAdapter = getAdapter();
        RecyclerView recyclerView = getRecyclerView();
        if (mAdapter == null || recyclerView == null) {
            return;
        }
        recyclerView.setAdapter(mAdapter);
        if (canLoadMore()) {
            mAdapter.setEnableLoadMore(true);
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadPage(mAdapter.getPageCount() + 1);
                }
            }, recyclerView);
        }

        if (loadOnInit() || !loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    protected void onRefreshing() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    public void setPageNo(int pageNo) {
        this.mPageNo = pageNo;
    }

    protected void reload() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    /**
     * 当前页数加载数据
     *
     * @param pageNo
     */
    protected void loadPage(final int pageNo) {
        if (getRecyclerView() == null || mAdapter == null) {
            return;
        }
        mPageNo = pageNo;
        getListObservable(pageNo)
                .filter(new Predicate<Result<List<T>>>() {
                    @Override
                    public boolean test(Result<List<T>> listResult) throws Exception {
                        return pageNo == mPageNo;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<T>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<T>> listResult) {
                        setRefreshing(false);
                        onDataLoaded(pageNo, listResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefreshing(false);
                        if (pageNo == DataConstants.FIRST_PAGE) {
                            if (keepListOnFail() && mAdapter.getDataCount() > 0) {
                                return;
                            }
                            setState(ViewState.EMPTY);
                        } else {
                            mAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * @param pageNo
     * @param result
     */
    protected void onDataLoaded(int pageNo, Result<List<T>> result) {
        List<T> data = result.data;
        if (pageNo == DataConstants.FIRST_PAGE) {
            if (ListUtils.isEmpty(data)) {
                setState(ViewState.EMPTY);
            } else {
                mAdapter.setNewData(data);
                if (data.size() < getPageSize()) {
                    mAdapter.setEnableLoadMore(false);
                } else {
                    mAdapter.setEnableLoadMore(true);
                }
                setState(ViewState.COMPLETED);
            }
        } else {
            if (ListUtils.isEmpty(data)) {
                mAdapter.loadMoreEnd(mAdapter.getData().size() < getPageSize() / 2);
            } else {
                mAdapter.addData(data);
                if (data.size() < getPageSize()) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }
        }
    }

    protected boolean loadOnInit() {
        return false;
    }

    protected boolean loadOnShow() {
        return true;
    }

    protected boolean keepListOnFail() {
        return true;
    }

    protected int getPageSize() {
        return DataConstants.DEFAULT_PAGE_SIZE;
    }

    protected abstract RecyclerView getRecyclerView();

    protected abstract BaseAdapter<T> getAdapter();

    protected abstract Observable<Result<List<T>>> getListObservable(int pageNo);

    protected abstract boolean canLoadMore();

    @Override
    protected abstract boolean canPullToRefresh();
}
