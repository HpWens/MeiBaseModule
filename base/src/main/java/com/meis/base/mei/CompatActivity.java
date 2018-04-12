package com.meis.base.mei;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meis.base.R;
import com.meis.base.mei.rxjava.UiSubscriber;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: ws4
 * created on: 2018/3/22 12:29
 * description:
 */
public abstract class CompatActivity extends RxAppCompatActivity implements IStatusHelper {

    protected String TAG = getClass().getSimpleName();

    private StatusHelper mStatusHelper = null;

    private Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        if (canStatusHelper()) {
            mStatusHelper = new StatusHelper(this, layoutResID);
            boolean refreshable = canPullToRefresh();
            boolean more = canPullToLoadMore();
            mStatusHelper.setup(refreshable, more);
            if (refreshable | more) {
                mStatusHelper.setRefreshHeader(getRefreshHeader());
                mStatusHelper.setOnRefreshListener(new StatusHelper.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        onRefreshing();
                    }

                    @Override
                    public void onLoadMore() {
                        onLoadingMore();
                    }
                });
            }
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void onCallContentView(@LayoutRes int layoutId) {
        super.setContentView(layoutId);
    }

    /**
     * type 注解 是否实现下拉刷新功能
     * <p>
     * PullToRefresh inject class judge refresh
     *
     * @return true pull to refresh otherwise false
     */
    protected boolean canPullToRefresh() {
        return getClass().isAnnotationPresent(PullToRefresh.class);
    }

    /**
     * type 注解 是否实现加载更多功能
     *
     * @return true pull load more otherwise false
     */
    protected boolean canPullToLoadMore() {
        return getClass().isAnnotationPresent(PullToLoadMore.class);
    }

    /**
     * 正在刷新添加异步的处理
     * <p>
     * loading refresh
     */
    protected void onRefreshing() {
        //handler refresh logic ...

    }

    /**
     * 加载更多添加异步处理
     * <p>
     * loading more
     */
    protected void onLoadingMore() {

    }

    /**
     * @param refreshing false auto refresh , true refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (canStatusHelper()) {
            mStatusHelper.setRefreshing(refreshing);
        }
    }

    /**
     * @param loadMore
     */
    public void setLoadingMore(boolean loadMore) {
        if (canStatusHelper()) {
            mStatusHelper.setLoadMore(loadMore);
        }
    }

    /**
     * 获取到 toolbar view
     *
     * @return
     */
    public Toolbar getToolbarView() {
        ensureToolbarView();
        return mToolbar;
    }

    /**
     * 初始化 toolbar
     */
    private void ensureToolbarView() {
        if (mToolbar == null) {
            setTitleLayout(R.layout.mei_toolbar);
            mToolbar = (Toolbar) mStatusHelper.getToolBar();
            //setSupportActionBar(mToolbar);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * @param layoutResId 设置 toolbar layout
     */
    public void setTitleLayout(@LayoutRes int layoutResId) {
        mStatusHelper.setTitleLayout(layoutResId);
    }

    /**
     * set state {@link #canStatusHelper()} before use!
     *
     * @see # setState(viewState,args)
     */
    public void setState(@ViewState.Val int viewState, Object... args) {
        if (canStatusHelper()) {
            mStatusHelper.showState(viewState, true, true, args);
        }
    }

    /***
     *
     * @param viewState
     * @param args
     */
    public void showState(@ViewState.Val int viewState, Object... args) {
        if (canStatusHelper()) {
            mStatusHelper.showState(viewState, true, false, args);
        }
    }

    /**
     * @param viewState
     */
    protected void hideState(@ViewState.Val int viewState) {
        if (canStatusHelper()) {
            mStatusHelper.showState(viewState, false, false);
        }
    }

    /**
     * 获取到空界面 View
     *
     * @return
     */
    public View getEmptyView() {
        return canStatusHelper() ? mStatusHelper.getEmptyView() : null;
    }

    /**
     * 获取到错误界面 View
     *
     * @return
     */
    public View getErrorView() {
        return canStatusHelper() ? mStatusHelper.getErrorView() : null;
    }

    /**
     * 获取到内容界面 View
     *
     * @return
     */
    public View getContentView() {
        return canStatusHelper() ? mStatusHelper.getContentView() : null;
    }

    /**
     * 获取正在加载界面 View
     *
     * @return
     */
    public View getLoadingView() {
        return canStatusHelper() ? mStatusHelper.getLoadingView() : null;
    }

    /**
     * 设置空界面布局
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        return canStatusHelper() ? mStatusHelper.setEmptyLayout(layoutResId) : null;
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(@StringRes int text) {
        if (canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(String text) {
        if (canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param icon
     */
    public void setEmptyIcon(@DrawableRes int icon) {
        if (canStatusHelper()) {
            mStatusHelper.setEmptyIcon(icon);
        }
    }

    /**
     * @param icon
     * @param text
     */
    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        if (canStatusHelper()) {
            mStatusHelper.setEmptyIconAndText(icon, text);
        }
    }

    /**
     * @param layoutResId
     * @return 正在加载界面 View
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        return canStatusHelper() ? mStatusHelper.setLoadingLayout(layoutResId) : null;
    }

    /**
     * @param layoutResId
     * @return 错误界面 View
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        return canStatusHelper() ? mStatusHelper.setErrorLayout(layoutResId) : null;
    }

    public RefreshHeader getRefreshHeader() {
        return new BezierRadarHeader(this);
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        mStatusHelper.setRefreshHeader(refreshHeader);
    }

    /**
     * auto refresh 自动刷新
     */
    public void autoRefresh() {
        mStatusHelper.autoRefresh();
    }

    @Override
    public void onErrorRetry() {

    }

    /**
     * 防止 handler 引起的内存泄漏
     *
     * @param delay
     * @param onNext
     */
    public void postUiThreads(long delay, UiSubscriber<Long> onNext) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    /**
     * @return false StatusHelper invalid
     */
    public boolean canStatusHelper() {
        return true;
    }

}
