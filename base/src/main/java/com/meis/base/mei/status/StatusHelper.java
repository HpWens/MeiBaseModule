package com.meis.base.mei.status;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.meis.base.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

/**
 * author: ws4
 * created on: 2018/3/22 12:43
 * description: 页面状态辅助类
 */
public class StatusHelper {

    private View mView;
    private View mContentView;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;

    /**
     * action bar view
     */
    private View mTitleView;

    private View mContentWrapView;

    /**
     * current state
     */
    private int mCurrentState = ViewState.REST;

    /**
     * refresh view
     */
    private SmartRefreshLayout mRefreshView;

    private int mContentLayoutId;

    private ViewGroup mFragmentContainer;

    private IStatusHelper mListener;

    private Context mContext;

    private StatusHelper() {
    }

    public StatusHelper(IStatusHelper listener, int contentLayoutId) {
        this.mListener = listener;
        this.mContentLayoutId = contentLayoutId;
    }

    public StatusHelper(IStatusHelper listener, ViewGroup fragmentContainer, int contentLayoutId) {
        this(listener, contentLayoutId);
        this.mFragmentContainer = fragmentContainer;
    }

    /**
     * set layout
     *
     * @param enableRefresh  是否下拉刷新
     * @param enableLoadMore 是否加载更多
     */
    public View setup(boolean enableRefresh, boolean enableLoadMore) {
        if (mView == null) {
            if (mListener instanceof Activity) {
                mListener.onSetContentView((enableRefresh | enableLoadMore) ? R.layout
                        .mei_base_with_refresh : R.layout.mei_base);
                mView = ((Activity) mListener).findViewById(R.id.base_main);
                mContext = mView.getContext();
            } else if (mListener instanceof Fragment) {
                mContext = ((Fragment) mListener).getContext();
                mView = LayoutInflater.from(mContext).inflate((enableRefresh | enableLoadMore) ?
                                R.layout.mei_base_with_refresh : R.layout.mei_base,
                        mFragmentContainer, false);
            } else {
                throw new RuntimeException("IStatusHelper must implements by Activity or " +
                        "fragment ! ");
            }
            setupContentView();
            if (enableRefresh | enableLoadMore) {
                mRefreshView = mView.findViewById(R.id.base_content_warp);
                mRefreshView.setEnableRefresh(enableRefresh);
                mRefreshView.setEnableLoadMore(enableLoadMore);
            }
            mContentWrapView = mView.findViewById(R.id.base_content_warp);
        }
        return mView;
    }

    public void setupContentView() {
        ViewStub contentStub = mView.findViewById(R.id.base_content_stub);
        if (contentStub != null && mContentLayoutId != 0) {
            contentStub.setLayoutResource(mContentLayoutId);
            mContentView = contentStub.inflate();
            Drawable background = mContentView.getBackground();
            if (background != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentView.setBackground(null);
                    mView.setBackground(background);
                }
            }
        }
    }

    /**
     * set toolbar layout
     *
     * @param layoutResId
     * @param barHeight
     * @return
     */
    public View setTitleLayout(@LayoutRes int layoutResId, int barHeight) {
        if (mTitleView != null) {
            return mTitleView;
        }
        setViewStubLayoutRes(R.id.base_action_bar_stub, layoutResId);
        mTitleView = inflateViewStub(R.id.base_action_bar_stub);
        if (mContentWrapView != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mContentWrapView
                    .getLayoutParams();
            lp.topMargin = barHeight;
        }
        return mTitleView;
    }

    public View setTitleLayout(@LayoutRes int layoutResId) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.mei_48_dp);
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext
                    .getResources().getDisplayMetrics());
        }
        return setTitleLayout(layoutResId, actionBarHeight);
    }

    /**
     * set empty layout
     *
     * @param layoutResId
     * @return
     */
    public View setEmptyLayout(@LayoutRes int layoutResId) {
        if (mEmptyView != null) return mEmptyView;
        setViewStubLayoutRes(R.id.base_empty_stub, layoutResId);
        mEmptyView = inflateViewStub(R.id.base_empty_stub);
        return mEmptyView;
    }

    /**
     * set loading layout
     *
     * @param layoutResId
     * @return
     */
    public View setLoadingLayout(@LayoutRes int layoutResId) {
        if (mLoadingView != null) return mLoadingView;
        setViewStubLayoutRes(R.id.base_loading_stub, layoutResId);
        mLoadingView = inflateViewStub(R.id.base_loading_stub);
        return mLoadingView;
    }

    /**
     * set error layout
     *
     * @param layoutResId
     * @return
     */
    public View setErrorLayout(@LayoutRes int layoutResId) {
        if (mErrorView != null) return mErrorView;
        setViewStubLayoutRes(R.id.base_error_stub, layoutResId);
        mErrorView = inflateViewStub(R.id.base_error_stub);
        return mErrorView;
    }

    public void setEmptyText(@StringRes int text) {
        ensureEmptyView();
        setEmptyArgs(text);
    }

    public void setEmptyText(String text) {
        ensureEmptyView();
        setEmptyArgs(text);
    }

    public void setEmptyIcon(@DrawableRes int icon) {
        ensureEmptyView();
        setEmptyArgs(icon);
    }

    public void setEmptyIconAndText(@DrawableRes int icon, @StringRes int text) {
        ensureEmptyView();
        setEmptyArgs(icon, text);
    }

    private void ensureEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = inflateViewStub(R.id.base_empty_stub);
        }
    }

    /**
     * set empty params
     *
     * @param args
     */
    private void setEmptyArgs(Object... args) {
        if (args != null && mEmptyView != null) {
            TextView view = mEmptyView.findViewById(R.id.tv_empty);
            setStatusArgs(view, args);
        }
    }

    /**
     * set status args
     *
     * @param view
     * @param args
     */
    private void setStatusArgs(TextView view, Object[] args) {
        if (view != null) {
            for (Object arg : args) {
                if (arg instanceof Integer) {
                    int resId = (int) arg;
                    String typeName = mContext.getResources().getResourceTypeName(resId);
                    if ("string".equals(typeName)) {
                        //文字
                        view.setText(resId);
                    } else {
                        //图标
                        view.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
                    }
                } else if (arg instanceof CharSequence) {
                    //文字
                    view.setText((CharSequence) arg);
                }
            }
        }
    }

    /**
     * set loading params
     *
     * @param args
     */
    private void setLoadingArgs(Object... args) {
        if (args != null && mLoadingView != null) {
            TextView view = mLoadingView.findViewById(R.id.tv_loading);
            setStatusArgs(view, args);
        }
    }

    /**
     * set error params
     *
     * @param args
     */
    private void setErrorArgs(Object... args) {
        if (args != null && mErrorView != null) {
            TextView view = mErrorView.findViewById(R.id.tv_error);
            setStatusArgs(view, args);
        }
    }

    public View getView() {
        return mView;
    }

    public View getTitleView() {
        return mTitleView;
    }

    public View getToolBar() {
        return mTitleView.findViewById(R.id.toolbar);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    /***
     * set action bar visible
     *
     * @param visible
     */
    public void setTitleViewVisible(boolean visible) {
        if (visible && mTitleView == null) {
            mTitleView = inflateViewStub(R.id.base_action_bar_stub);
        }
        if (mTitleView != null) {
            mTitleView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @param viewState
     * @param show
     * @param hideOther
     * @param args
     */
    public void showState(int viewState, boolean show, boolean hideOther, Object... args) {
        if (hideOther) {
            setContentViewVisible(ViewState.COMPLETED == viewState & show);
            setLoadingViewVisible(ViewState.LOADING == viewState & show, ViewState.LOADING ==
                    viewState ? args : null);
            setErrorViewVisible(ViewState.ERROR == viewState & show, ViewState.ERROR == viewState
                    ? args : null);
            setEmptyViewVisible(ViewState.EMPTY == viewState & show, ViewState.EMPTY == viewState
                    ? args : null);
            setRefreshing(ViewState.REFRESH == viewState & show);
        } else {
            switch (viewState) {
                case ViewState.COMPLETED:
                    setContentViewVisible(show);
                    break;
                case ViewState.EMPTY:
                    setEmptyViewVisible(show, args);
                    break;
                case ViewState.ERROR:
                    setErrorViewVisible(show);
                    break;
                case ViewState.LOADING:
                    setLoadingViewVisible(show, args);
                    break;
                case ViewState.REFRESH:
                    setRefreshing(show);
                    break;
            }
        }
        mCurrentState = viewState;
    }

    private void setContentViewVisible(boolean visible) {
        if (visible && mContentView == null) {
            mContentView = inflateViewStub(R.id.base_content_stub);
        }
        if (mContentView != null) {
            mContentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setLoadingViewVisible(boolean visible, Object... args) {
        if (visible && mLoadingView == null) {
            mLoadingView = inflateViewStub(R.id.base_loading_stub);
        }
        if (mLoadingView != null) {
            if (args != null) {
                setLoadingArgs(args);
            }
            mLoadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setErrorViewVisible(boolean visible, Object... args) {
        if (visible && mErrorView == null) {
            mErrorView = inflateViewStub(R.id.base_error_stub);
        }
        if (mErrorView != null) {
            if (args != null) {
                setErrorArgs(args);
            }
            if (visible) {
                mErrorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onErrorRetry();
                        }
                    }
                });
            }
            mErrorView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setEmptyViewVisible(boolean visible, Object... args) {
        if (visible && mEmptyView == null) {
            mEmptyView = inflateViewStub(R.id.base_empty_stub);
        }
        if (mEmptyView != null) {
            if (args != null) {
                setEmptyArgs(args);
            }
            mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshView != null) {
            if (!refreshing && mRefreshView.isEnableRefresh()) {
                mRefreshView.finishRefresh();
            }
        }
    }

    public void setLoadMore(boolean loadMore) {
        if (mRefreshView != null) {
            if (mRefreshView.isEnableRefresh() && !loadMore) {
                mRefreshView.finishLoadMore();
            }
        }
    }

    public void setOnRefreshListener(final OnRefreshListener listener) {
        if (mRefreshView != null) {
            mRefreshView.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener
                    .OnRefreshListener() {

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mRefreshView.autoRefresh();
                    listener.onRefresh();
                }
            });
            mRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mRefreshView.autoLoadMore();
                    listener.onLoadMore();
                }
            });
        }
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    private void setViewStubLayoutRes(@IdRes int stubId, @LayoutRes int layoutResId) {
        ViewStub viewStub = (ViewStub) mView.findViewById(stubId);
        if (viewStub != null) {
            viewStub.setLayoutResource(layoutResId);
        }
    }

    private View inflateViewStub(@IdRes int stubId) {
        ViewStub viewStub = (ViewStub) mView.findViewById(stubId);
        if (viewStub != null && viewStub.getLayoutResource() != 0) {
            return viewStub.inflate();
        }
        return null;
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        if (mRefreshView != null) {
            mRefreshView.setRefreshHeader(refreshHeader);
        }
    }

    public void autoRefresh() {
        if (mRefreshView != null) {
            mRefreshView.autoRefresh();
        }
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

}
