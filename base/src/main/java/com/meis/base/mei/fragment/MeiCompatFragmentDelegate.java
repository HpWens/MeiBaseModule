package com.meis.base.mei.fragment;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import com.meis.base.R;
import com.meis.base.mei.MeiCompatActivityDelegate;
import com.meis.base.mei.status.IStatusHelper;
import com.meis.base.mei.status.StatusHelper;
import com.meis.base.mei.status.ViewState;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * desc: 碎片委托类
 * author: ws
 * date: 2018/4/19.
 */
public class MeiCompatFragmentDelegate {
    private IMeiCompatFragment mMeiCompatFragment;
    private Fragment mFragment;

    private StatusHelper mStatusHelper = null;

    private Toolbar mToolbar;

    private boolean mKeyboardVisible = false;

    private MeiCompatActivityDelegate.WeakHandler mHandler;

    public MeiCompatFragmentDelegate(IMeiCompatFragment meiCompatFragment) {
        if (!(meiCompatFragment instanceof Fragment)) {
            throw new RuntimeException("Must extends Fragment");
        }
        this.mMeiCompatFragment = meiCompatFragment;
        this.mFragment = (Fragment) meiCompatFragment;
    }

    public View onCreateView(int layoutId, @Nullable ViewGroup container, IStatusHelper statusHelper) {
        if (null == mStatusHelper) {
            mStatusHelper = new StatusHelper(statusHelper, container, layoutId);
        }
        boolean refreshable = mMeiCompatFragment.canPullToRefresh();
        boolean more = mMeiCompatFragment.canPullToLoadMore();
        View view = mStatusHelper.setup(refreshable, more);
        if (refreshable | more) {
            mStatusHelper.setRefreshHeader(mMeiCompatFragment.getRefreshHeader());
            mStatusHelper.setOnRefreshListener(new StatusHelper.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mMeiCompatFragment.onRefreshing();
                }

                @Override
                public void onLoadMore() {
                    mMeiCompatFragment.onLoadingMore();
                }
            });
        }
        return view;
    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setRefreshing(refreshing);
        }
    }

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param loadMore
     */
    public void setLoadingMore(boolean loadMore) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setLoadMore(loadMore);
        }
    }

    public Toolbar getToolbarView() {
        ensureToolbarView();
        return mToolbar;
    }

    public void setToolbarLayout(@LayoutRes int layoutResId) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setTitleLayout(layoutResId);
        }
    }

    private void ensureToolbarView() {
        if (mToolbar == null) {
            setToolbarLayout(R.layout.mei_toolbar);
            mToolbar = (Toolbar) mStatusHelper.getToolBar();
            //setSupportActionBar(mToolbar);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    /**
     * set state {@link # mMeiCompatActivity.canStatusHelper()} before use!
     *
     * @see # setState(viewState,args)
     */
    public void setState(@ViewState.Val int viewState, Object... args) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.showState(viewState, true, true, args);
        }
    }

    /***
     *
     * @param viewState
     * @param args
     */
    public void showState(@ViewState.Val int viewState, Object... args) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.showState(viewState, true, false, args);
        }
    }

    /**
     * @param viewState
     */
    protected void hideState(@ViewState.Val int viewState) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.showState(viewState, false, false);
        }
    }

    /**
     * 获取到空界面 View
     *
     * @return
     */
    public View getEmptyView() {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.getEmptyView() : null;
    }

    /**
     * 获取到错误界面 View
     *
     * @return
     */
    public View getErrorView() {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.getErrorView() : null;
    }

    /**
     * 获取正在加载界面 View
     *
     * @return
     */
    public View getLoadingView() {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.getLoadingView() : null;
    }

    /**
     * 获取到内容界面 View
     *
     * @return
     */
    public View getContentView() {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.getContentView() : null;
    }

    /**
     * 设置空界面布局
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.setEmptyLayout(layoutResId) : null;
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(@StringRes int text) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(String text) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param icon
     */
    public void setEmptyIcon(@DrawableRes int icon) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setEmptyIcon(icon);
        }
    }

    /**
     * @param icon
     * @param text
     */
    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setEmptyIconAndText(icon, text);
        }
    }

    /**
     * @param layoutResId
     * @return 正在加载界面 View
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.setLoadingLayout(layoutResId) : null;
    }

    /**
     * @param layoutResId
     * @return 错误界面 View
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        return mMeiCompatFragment.canStatusHelper() ? mStatusHelper.setErrorLayout(layoutResId) : null;
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.setRefreshHeader(refreshHeader);
        }
    }

    /**
     * auto refresh 自动刷新
     */
    public void autoRefresh() {
        if (mMeiCompatFragment.canStatusHelper()) {
            mStatusHelper.autoRefresh();
        }
    }

    /**
     * 防止 handler 引起的内存泄漏 处理成静态+弱引用
     * <p>
     *
     * @param delay
     * @param runnable
     */
    public void postUiThread(Runnable runnable, long delay) {
        if (mHandler == null) {
            mHandler = new MeiCompatActivityDelegate.WeakHandler(mFragment.getActivity());
        }
        mHandler.postDelayed(runnable, delay);
    }

    protected void onDetach() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        return (T) mFragment.getView().findViewById(id);
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
}
