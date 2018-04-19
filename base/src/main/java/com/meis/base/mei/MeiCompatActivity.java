package com.meis.base.mei;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.meis.base.mei.annotation.PullToLoadMore;
import com.meis.base.mei.annotation.PullToRefresh;
import com.meis.base.mei.status.IStatusHelper;
import com.meis.base.mei.status.ViewState;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

/**
 * desc: https://github.com/HpWens/MeiBaseModule
 * author: ws
 * date: 2018/4/19.
 */
public class MeiCompatActivity extends AppCompatActivity implements IMeiCompatActivity, IStatusHelper {

    final MeiCompatActivityDelegate mDelegate = new MeiCompatActivityDelegate(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onSetContentView(int layoutId) {
        super.setContentView(layoutId);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (canStatusHelper()) {
            mDelegate.setContentView(this, layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void onErrorRetry() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return new BezierRadarHeader(this);
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        mDelegate.setRefreshHeader(refreshHeader);
    }

    @Override
    public boolean canStatusHelper() {
        return true;
    }

    /**
     * @return 软键盘是否打开
     */
    public boolean isKeyboardOpened() {
        return mDelegate.isKeyboardOpened();
    }

    /**
     * 监听软键盘的状态
     * <p>
     * {@link #onCreate(Bundle)}内部调用
     */
    protected void enableKeyboardVisibilityListener() {
        mDelegate.enableKeyboardVisibilityListener();
    }

    @Override
    public void onKeyboardVisibilityChanged(boolean visibility) {

    }

    @Override
    public boolean canPullToRefresh() {
        return getClass().isAnnotationPresent(PullToRefresh.class);
    }

    @Override
    public boolean canPullToLoadMore() {
        return  getClass().isAnnotationPresent(PullToLoadMore.class);
    }

    /**
     * 防止 handler 引起的内存泄漏 处理成静态+弱引用
     * <p>
     *
     * @param delay
     * @param runnable
     */
    public void postUiThread(Runnable runnable, long delay) {
        mDelegate.postUiThread(runnable, delay);
    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        mDelegate.setRefreshing(refreshing);
    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param loadMore
     */
    public void setLoadingMore(boolean loadMore) {
        mDelegate.setLoadingMore(loadMore);
    }

    /**
     * 获取 toolbar 的布局 view
     *
     * @return
     */
    public Toolbar getToolbarView() {
        return mDelegate.getToolbarView();
    }

    /**
     * @param layoutResId
     */
    public void setToolbarLayout(@LayoutRes int layoutResId) {
        mDelegate.setToolbarLayout(layoutResId);
    }

    /**
     * @param layoutResId
     * @return 正在加载界面 View
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        return mDelegate.setLoadingLayout(layoutResId);
    }

    /**
     * @param layoutResId
     * @return 错误界面 View
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        return mDelegate.setErrorLayout(layoutResId);
    }

    /**
     * 设置空界面布局
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        return mDelegate.setEmptyLayout(layoutResId);
    }

    /**
     * set state {@link #canStatusHelper()} before use!
     *
     * @see # setState(viewState,args)
     */
    public void setState(@ViewState.Val int viewState, Object... args) {
        mDelegate.setState(viewState, args);
    }

    /***
     *
     * @param viewState
     * @param args
     */
    public void showState(@ViewState.Val int viewState, Object... args) {
        mDelegate.showState(viewState, args);
    }

    /**
     * @param viewState
     */
    protected void hideState(@ViewState.Val int viewState) {
        mDelegate.hideState(viewState);
    }

    /**
     * 获取到空界面 View
     *
     * @return
     */
    public View getEmptyView() {
        return mDelegate.getEmptyView();
    }

    /**
     * 获取到错误界面 View
     *
     * @return
     */
    public View getErrorView() {
        return mDelegate.getErrorView();
    }

    /**
     * 获取到内容界面 View
     *
     * @return
     */
    public View getContentView() {
        return mDelegate.getContentView();
    }

    /**
     * 获取正在加载界面 View
     *
     * @return
     */
    public View getLoadingView() {
        return mDelegate.getLoadingView();
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(@StringRes int text) {
        mDelegate.setEmptyText(text);
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(String text) {
        mDelegate.setEmptyText(text);
    }

    /**
     * @param icon
     */
    public void setEmptyIcon(@DrawableRes int icon) {
        mDelegate.setEmptyIcon(icon);
    }

    /**
     * @param icon
     * @param text
     */
    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        mDelegate.setEmptyIconAndText(icon, text);
    }

    /**
     * auto refresh 自动刷新
     */
    public void autoRefresh() {
        mDelegate.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }
}
