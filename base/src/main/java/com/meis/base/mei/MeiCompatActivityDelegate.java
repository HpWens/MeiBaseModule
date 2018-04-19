package com.meis.base.mei;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.meis.base.R;import com.meis.base.mei.fragment.IMeiCompatFragment;
import com.meis.base.mei.status.IStatusHelper;
import com.meis.base.mei.status.StatusHelper;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.SoftKeyboardUtils;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * desc: 委托类
 * author: ws
 * date: 2018/4/19.
 */

public class MeiCompatActivityDelegate {
    private IMeiCompatActivity mMeiCompatActivity;
    private FragmentActivity mActivity;

    private Toolbar mToolbar;

    //soft keyboard status (open or close)
    private boolean mKeyboardOpened = false;

    private View.OnLayoutChangeListener mLayoutChangeListener;

    private StatusHelper mStatusHelper = null;

    private WeakHandler mHandler;

    public MeiCompatActivityDelegate(IMeiCompatActivity compatActivity) {
        if (!(compatActivity instanceof FragmentActivity)) {
            throw new RuntimeException("Must extends FragmentActivity/AppCompatActivity");
        }
        this.mMeiCompatActivity = compatActivity;
        this.mActivity = (FragmentActivity) compatActivity;
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity.setTheme(R.style.MeiBaseTheme);
    }


    public void setContentView(IStatusHelper statusHelper, int layoutResID) {
        if (null == mStatusHelper) {
            mStatusHelper = new StatusHelper(statusHelper, layoutResID);
        }
        boolean refreshable = mMeiCompatActivity.canPullToRefresh();
        boolean more = mMeiCompatActivity.canPullToLoadMore();
        mStatusHelper.setup(refreshable, more);
        if (refreshable | more) {
            mStatusHelper.setRefreshHeader(mMeiCompatActivity.getRefreshHeader());
            mStatusHelper.setOnRefreshListener(new StatusHelper.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mMeiCompatActivity.onRefreshing();
                }

                @Override
                public void onLoadMore() {
                    mMeiCompatActivity.onLoadingMore();
                }
            });
        }
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mKeyboardOpened && ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (dispatchChildView((ViewGroup) getContentView(), x, y)) {
                return false;
            }
            View focusView = mActivity.getCurrentFocus();
            if (focusView != null) {
                View parent = (View) focusView.getParent();
                String tag = (String) parent.getTag();
                if (null != tag && tag.equals("input")) {
                    focusView = parent;
                }
            }
            if (isShouldHideKeyboard(focusView, x, y)) {
                SoftKeyboardUtils.closeSoftKeyboard(mActivity);
                return true;
            }
        }
        return false;
    }

    /**
     * @param contentView
     * @param x           current touch point.x
     * @param y           current touch point.y
     * @return
     */
    private boolean dispatchChildView(ViewGroup contentView, int x, int y) {
        boolean isDispatch = false;
        for (int i = contentView.getChildCount() - 1; i >= 0; i--) {
            View childView = contentView.getChildAt(i);
            boolean touchView = isTouchRegion(x, y, childView);
            if (!childView.isShown()) {
                continue;
            }
            if (touchView && "dispatch".equals(childView.getTag())) {
                isDispatch = true;
                break;
            }
            if (childView instanceof ViewGroup) {
                ViewGroup itemView = (ViewGroup) childView;
                if (!touchView) {
                    continue;
                } else {
                    isDispatch |= dispatchChildView(itemView, x, y);
                    break;
                }
            }
        }
        return isDispatch;
    }

    /**
     * @param x    current touch point.x
     * @param y    current touch point.y
     * @param view
     * @return
     */
    private boolean isTouchRegion(int x, int y, View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.contains(x, y);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     * <p>
     * 如果当用户点击EditText时则不能隐藏
     * <p>
     * 如果焦点不是EditText则忽略，这个发生在视图刚绘制完
     * <p>
     * 第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
     *
     * @param v
     * @param x current touch point.x
     * @param y current touch point.y
     * @return
     */
    private boolean isShouldHideKeyboard(View v, int x, int y) {
        if (v != null) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            return !rect.contains(x, y);
        }
        return false;
    }

    /**
     * @return 软键盘是否打开
     */
    public boolean isKeyboardOpened() {
        return mKeyboardOpened;
    }

    /**
     * 监听软键盘的状态
     * <p>
     * {@link #onCreate(Bundle)}内部调用
     */
    protected void enableKeyboardVisibilityListener() {
        mActivity.getWindow().getDecorView().addOnLayoutChangeListener(mLayoutChangeListener = new View
                .OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int
                    oldRight, int oldBottom) {
                Rect rect = new Rect();
                mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 0) {
                    mKeyboardOpened = false;
                } else {
                    mKeyboardOpened = true;
                }
                mMeiCompatActivity.onKeyboardVisibilityChanged(mKeyboardOpened);
                notifyKeyboardVisibilityChanged(mKeyboardOpened);
            }
        });
    }

    /**
     * 通知软键盘状态发生了改变
     *
     * @param keyboardOpened
     */
    private void notifyKeyboardVisibilityChanged(boolean keyboardOpened) {
        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof IMeiCompatFragment) {
                IMeiCompatFragment compatFragment = (IMeiCompatFragment) fragment;
                if (compatFragment.getKeyboardVisible()) {
                    compatFragment.onKeyboardVisibilityChanged(keyboardOpened);
                }
            }
        }
    }


    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (mMeiCompatActivity.canStatusHelper()) {
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
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setLoadMore(loadMore);
        }
    }

    public Toolbar getToolbarView() {
        ensureToolbarView();
        return mToolbar;
    }

    public void setToolbarLayout(@LayoutRes int layoutResId) {
        if (mMeiCompatActivity.canStatusHelper()) {
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
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.showState(viewState, true, true, args);
        }
    }

    /***
     *
     * @param viewState
     * @param args
     */
    public void showState(@ViewState.Val int viewState, Object... args) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.showState(viewState, true, false, args);
        }
    }

    /**
     * @param viewState
     */
    protected void hideState(@ViewState.Val int viewState) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.showState(viewState, false, false);
        }
    }

    /**
     * 获取到空界面 View
     *
     * @return
     */
    public View getEmptyView() {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.getEmptyView() : null;
    }

    /**
     * 获取到错误界面 View
     *
     * @return
     */
    public View getErrorView() {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.getErrorView() : null;
    }

    /**
     * 获取正在加载界面 View
     *
     * @return
     */
    public View getLoadingView() {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.getLoadingView() : null;
    }

    /**
     * 获取到内容界面 View
     *
     * @return
     */
    public View getContentView() {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.getContentView() : null;
    }

    /**
     * 设置空界面布局
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.setEmptyLayout(layoutResId) : null;
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(@StringRes int text) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param text 提示语
     */
    public void setEmptyText(String text) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setEmptyText(text);
        }
    }

    /**
     * @param icon
     */
    public void setEmptyIcon(@DrawableRes int icon) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setEmptyIcon(icon);
        }
    }

    /**
     * @param icon
     * @param text
     */
    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setEmptyIconAndText(icon, text);
        }
    }

    /**
     * @param layoutResId
     * @return 正在加载界面 View
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.setLoadingLayout(layoutResId) : null;
    }

    /**
     * @param layoutResId
     * @return 错误界面 View
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        return mMeiCompatActivity.canStatusHelper() ? mStatusHelper.setErrorLayout(layoutResId) : null;
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        if (mMeiCompatActivity.canStatusHelper()) {
            mStatusHelper.setRefreshHeader(refreshHeader);
        }
    }

    /**
     * auto refresh 自动刷新
     */
    public void autoRefresh() {
        if (mMeiCompatActivity.canStatusHelper()) {
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
            mHandler = new WeakHandler(mActivity);
        }
        mHandler.postDelayed(runnable, delay);
    }

    protected void onDestroy() {
        if (mLayoutChangeListener != null) {
            mActivity.getWindow().getDecorView().removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 防止handler的泄露
     */
    public static class WeakHandler extends Handler {
        private WeakReference<FragmentActivity> activityWeakReference;

        public WeakHandler(FragmentActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = activityWeakReference.get();
            if (activity != null) {
            }
        }
    }
}
