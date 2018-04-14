package com.meis.base.mei.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meis.base.R;
import com.meis.base.mei.IStatusHelper;
import com.meis.base.mei.PullToLoadMore;
import com.meis.base.mei.PullToRefresh;
import com.meis.base.mei.StatusHelper;
import com.meis.base.mei.ViewState;
import com.meis.base.mei.dialog.BaseDialog;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * author: ws4
 * created on: 2018/4/8 14:36
 * description:
 */
public abstract class CompatFragment extends RxFragment implements IStatusHelper {

    protected final String TAG = getClass().getSimpleName();

    private StatusHelper mStatusHelper = null;

    private Toolbar mToolbar;

    private boolean mUserVisibleHint = true;

    private boolean mKeyboardVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        mStatusHelper = new StatusHelper(this, container, layoutId);
        boolean refreshable = canPullToRefresh();
        boolean more = canPullToLoadMore();
        View view = mStatusHelper.setup(refreshable, more);
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    /**
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }

    @Override
    public void onCallContentView(int layoutId) {

    }

    /**
     * 启动软键盘的监听
     */
    protected void enableKeyboardVisibleListener() {
        mKeyboardVisible = true;
    }

    public boolean getKeyboardVisible() {
        return mKeyboardVisible;
    }

    /**
     * @param visibility 软键盘的打开/关闭
     */
    public void onKeyboardVisibilityChanged(boolean visibility) {

    }

    /**
     * type 一行代码实现下拉刷新
     * <p>
     * PullToRefresh inject class judge refresh
     *
     * @return true pull to refresh otherwise false
     */
    protected boolean canPullToRefresh() {
        return getClass().isAnnotationPresent(PullToRefresh.class);
    }

    /**
     * type 一行代码实现上拉加载
     *
     * @return true pull load more otherwise false
     */
    protected boolean canPullToLoadMore() {
        return getClass().isAnnotationPresent(PullToLoadMore.class);
    }

    /**
     * refreshing adding asynchronous processing
     * <p>
     * loading refresh
     */
    protected void onRefreshing() {
        //handler refresh logic ...

    }

    /**
     * load more to add asynchronous processing
     * <p>
     * loading more
     */
    protected void onLoadingMore() {

    }

    @Override
    public void onErrorRetry() {

    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        mStatusHelper.setRefreshing(refreshing);
    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param loadMore
     */
    public void setLoadingMore(boolean loadMore) {
        mStatusHelper.setLoadMore(loadMore);
    }


    public Toolbar getToolbarView() {
        ensureToolbarView();
        return mToolbar;
    }

    private void ensureToolbarView() {
        if (mToolbar == null) {
            setTitleLayout(R.layout.mei_toolbar);
            mToolbar = (Toolbar) mStatusHelper.getToolBar();
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * @param layoutResId 设置 toolbar layout
     */
    public void setTitleLayout(@LayoutRes int layoutResId) {
        mStatusHelper.setTitleLayout(layoutResId);
    }

    /**
     * set state {@link # onCreateView()} before use!
     *
     * @see # setState(viewState,args)
     */
    public void setState(@ViewState.Val int viewState, Object... args) {
        mStatusHelper.showState(viewState, true, true, args);
    }

    /**
     * @param viewState
     * @param args
     */
    public void showState(@ViewState.Val int viewState, Object... args) {
        mStatusHelper.showState(viewState, true, false, args);
    }

    /**
     * @param viewState
     */
    protected void hideState(@ViewState.Val int viewState) {
        mStatusHelper.showState(viewState, false, false);
    }

    /**
     * @return 获取到空界面 View
     */
    public View getEmptyView() {
        return mStatusHelper.getEmptyView();
    }

    /**
     * @return 获取到错误界面 View
     */
    public View getErrorView() {
        return mStatusHelper.getErrorView();
    }

    /**
     * @return 获取到内容界面 View
     */
    public View getContentView() {
        return mStatusHelper.getContentView();
    }

    /**
     * @return 获取正在加载界面 View
     */
    public View getLoadingView() {
        return mStatusHelper.getLoadingView();
    }

    /**
     * 设置空界面布局
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        return mStatusHelper.setEmptyLayout(layoutResId);
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(@StringRes int text) {
        mStatusHelper.setEmptyText(text);
    }

    /**
     * @param text
     */
    public void setEmptyText(String text) {
        mStatusHelper.setEmptyText(text);
    }

    /**
     * @param icon
     */
    public void setEmptyIcon(@DrawableRes int icon) {
        mStatusHelper.setEmptyIcon(icon);
    }

    /**
     * @param icon
     * @param text
     */
    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        mStatusHelper.setEmptyIconAndText(icon, text);
    }

    /**
     * @param layoutResId
     * @return 正在加载界面 View
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        return mStatusHelper.setLoadingLayout(layoutResId);
    }

    /**
     * @param layoutResId
     * @return 错误界面 View
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        return mStatusHelper.setErrorLayout(layoutResId);
    }

    /**
     * 防止 handler 引起的内存泄漏
     * <p>
     * 这里没有使用Consumer是由于事件销毁不走accept方法
     *
     * @param delay
     * @param onNext
     */
    public void postUiThread(long delay, Consumer<Long> onNext) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    /**
     * 重写该方法
     *
     * @return 下拉刷新样式
     */
    public RefreshHeader getRefreshHeader() {
        return new BezierRadarHeader(getActivity());
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

    /**
     * @param baseDialog
     */
    public void showDialog(BaseDialog baseDialog) {
        getActivity().getSupportFragmentManager().beginTransaction().add(baseDialog, "dialog_" + baseDialog.getClass
                ().getSimpleName()).commitAllowingStateLoss();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();
}
