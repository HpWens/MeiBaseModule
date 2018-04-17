package com.meis.base.mei;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.meis.base.R;
import com.meis.base.mei.dialog.MeiBaseDialog;
import com.meis.base.mei.fragment.MeiCompatFragment;
import com.meis.base.mei.rxjava.UiSubscriber;
import com.meis.base.mei.utils.SoftKeyboardUtils;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: ws4
 * created on: 2018/3/22 12:29
 * description:
 */
public abstract class MeiCompatActivity extends RxAppCompatActivity implements IStatusHelper {

    protected String TAG = getClass().getSimpleName();

    private StatusHelper mStatusHelper = null;

    private Toolbar mToolbar;

    //soft keyboard status (open or close)
    private boolean mKeyboardOpened;

    private View.OnLayoutChangeListener mLayoutChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MeiBaseTheme);
        setContentView(layoutResId());
        onSavedInstanceState(savedInstanceState);
        //初始化View
        initView();
        //初始化数据
        initData();
    }

    protected void onSavedInstanceState(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCallContentView(@LayoutRes int layoutId) {
        super.setContentView(layoutId);
    }


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

    /**
     * false true 被消费
     * <p>
     * super.dispatchTouchEvent(ev)继续分发
     * <p>
     * view.setTag("input") or android:tag="input" 如果当前软键盘是弹起状态，点击该view外的任何区域则会收起软键盘
     * <p>
     * 如果你不想收起软键盘，请设置 view.setTag("dispatch") or android:tag="dispatch"
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mKeyboardOpened && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (dispatchChildView((ViewGroup) getContentView(), (int) ev.getX(), (int) ev.getY())) {
                return super.dispatchTouchEvent(ev);
            }
            View focusView = getCurrentFocus();
            if (focusView != null) {
                View parent = (View) focusView.getParent();
                String tag = (String) parent.getTag();
                if (null != tag && tag.equals("input")) {
                    focusView = parent;
                }
            }
            if (isShouldHideKeyboard(focusView, (int) ev.getX(), (int) ev.getY())) {
                SoftKeyboardUtils.closeSoftKeyboard(this);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
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
     * {@link #initView()#initData()}内部调用
     */
    protected void enableKeyboardVisibilityListener() {
        getWindow().getDecorView().addOnLayoutChangeListener(mLayoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int
                    oldRight, int oldBottom) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 0) {
                    mKeyboardOpened = false;
                } else {
                    mKeyboardOpened = true;
                }
                onKeyboardVisibilityChanged(mKeyboardOpened);
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
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof MeiCompatFragment) {
                MeiCompatFragment compatFragment = (MeiCompatFragment) fragment;
                if (compatFragment.getKeyboardVisible()) {
                    compatFragment.onKeyboardVisibilityChanged(keyboardOpened);
                }
            }
        }
    }

    /**
     * @param visibility true键盘的打开/false关闭
     */
    protected void onKeyboardVisibilityChanged(boolean visibility) {

    }

    @Override
    protected void onDestroy() {
        if (mLayoutChangeListener != null) {
            getWindow().getDecorView().removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        super.onDestroy();
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

    /**
     * false 完成刷新
     * <p>
     * true  保留上次状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (canStatusHelper()) {
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
        if (canStatusHelper()) {
            mStatusHelper.setLoadMore(loadMore);
        }
    }

    public Toolbar getToolbarView() {
        ensureToolbarView();
        return mToolbar;
    }

    private void ensureToolbarView() {
        if (mToolbar == null) {
            setTitleLayout(R.layout.mei_toolbar);
            mToolbar = (Toolbar) mStatusHelper.getToolBar();
            //setSupportActionBar(mToolbar);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * @param layoutResId
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

    /**
     * 重写该方法
     *
     * @return 下拉刷新样式
     */
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
     * <p>
     * 这里没有使用Consumer是由于事件销毁不走accept方法
     *
     * @param delay
     * @param onNext
     */
    public void postUiThread(long delay, UiSubscriber<Long> onNext) {
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

    /**
     * @param baseDialog
     */
    public void showDialog(MeiBaseDialog baseDialog) {
        getSupportFragmentManager().beginTransaction().add(baseDialog, "dialog_" + baseDialog.getClass
                ().getSimpleName()).commitAllowingStateLoss();
    }

    /**
     * 初始化控件
     * <p>
     * init view
     */
    protected abstract void initView();

    /**
     * 初始化数据 填充数据 异步请求
     * <p>
     * init data
     */
    protected abstract void initData();

    /**
     * 填充布局资源
     *
     * @return
     */
    protected abstract int layoutResId();

}
