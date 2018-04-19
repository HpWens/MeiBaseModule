package com.meis.base.mei;

import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * desc:
 * author: ws
 * date: 2018/4/19.
 */

public interface IMeiCompatActivity {
    /**
     * refreshing adding asynchronous processing
     * <p>
     * loading refresh
     */
    void onRefreshing();

    /**
     * load more to add asynchronous processing
     * <p>
     * loading more
     */
    void onLoadingMore();

    /**
     * 重写该方法
     *
     * @return 获取下拉刷新样式
     */
    RefreshHeader getRefreshHeader();

    /**
     * 返回false 状态界面会失效 刷新功能会失效
     *
     * @return
     */
    boolean canStatusHelper();

    /**
     * @param visibility true键盘的打开/false关闭
     */
    void onKeyboardVisibilityChanged(boolean visibility);

    /**
     * type 一行代码实现下拉刷新
     * <p>
     * PullToRefresh inject class judge refresh
     *
     * @return true pull to refresh otherwise false
     */
    boolean canPullToRefresh();

    /**
     * type 一行代码实现上拉加载
     *
     * @return true pull load more otherwise false
     */
    boolean canPullToLoadMore();
}
