package com.meis.base.mei;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
 * created on: 2018/4/11 18:14
 * description: 基类列表活动
 */
public abstract class BaseListActivity<T> extends BaseActivity {

    protected BaseAdapter<T> mAdapter;

    //当前页码
    private int mPageNo;

    @Override
    protected void onSavedInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSavedInstanceState(savedInstanceState);
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
        //是否首次加载 是否每次可见加载
        if (loadOnInit() || !loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    protected void onRefreshing() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    /**
     * 设置当前页码
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        this.mPageNo = pageNo;
    }

    /**
     * 重新加载
     */
    protected void reload() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    /**
     * 按页码加载数据 默认一页大小20 可以重写{@link #getPageSize()} 的返回页码大小
     *
     * @param pageNo
     */
    protected void loadPage(final int pageNo) {
        if (mAdapter == null) {
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
                            if (keepEmptyOnFirstPage() && mAdapter.getDataCount() > 0) {
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

    /**
     * @return
     */
    protected boolean loadOnInit() {
        return false;
    }

    /**
     * 每次界面重新显示的时候 是否加载数据
     * <br/>
     * true 加载
     * false 不加载
     *
     * @return
     */
    protected boolean loadOnShow() {
        return true;
    }

    /**
     * false 默认第一页数据为空或加载失败 显示空界面
     * true 不显示空界面
     *
     * @return false
     */
    protected boolean keepEmptyOnFirstPage() {
        return false;
    }

    /**
     * 可以重写该方法 返回每页大小
     *
     * @return
     */
    protected int getPageSize() {
        return DataConstants.DEFAULT_PAGE_SIZE;
    }

    protected abstract RecyclerView getRecyclerView();

    protected abstract BaseAdapter<T> getAdapter();

    /**
     * Result 实体类下个版本会优化
     *
     * @param pageNo
     * @return
     */
    protected abstract Observable<Result<List<T>>> getListObservable(int pageNo);

    /**
     * 是否可以上拉加载更多
     *
     * @return
     */
    protected abstract boolean canLoadMore();

    /**
     * 是否可以下拉刷新
     *
     * @return
     */
    @Override
    protected abstract boolean canPullToRefresh();
}
