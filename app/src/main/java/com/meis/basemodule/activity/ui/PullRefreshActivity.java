package com.meis.basemodule.activity.ui;

import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.meis.base.mei.BaseActivity;
import com.meis.base.mei.PullToLoadMore;
import com.meis.base.mei.PullToRefresh;
import com.meis.base.mei.ViewState;
import com.meis.base.mei.header.DingDangHeader;
import com.meis.base.mei.rxjava.UiSubscriber;
import com.meis.basemodule.R;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * author: ws4
 * created on: 2018/4/11 15:32
 * description:
 */
@PullToRefresh
@PullToLoadMore
public class PullRefreshActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.refresh));
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setState(ViewState.EMPTY, new Object[]{getString(R.string.drag_refresh)});

        getToolbarView().inflateMenu(R.menu.type);
        getToolbarView().setOnMenuItemClickListener(this);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onRefreshing() {
        super.onRefreshing();
        postUiThreads(2000, new UiSubscriber<Long>() {
            @Override
            public void onCompleted() {
                Toast.makeText(PullRefreshActivity.this, getResources().getString(R.string
                        .mei_refresh_success), Toast.LENGTH_SHORT).show();
                PullRefreshActivity.this.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onLoadingMore() {
        super.onLoadingMore();
        postUiThreads(2000, new UiSubscriber<Long>() {
            @Override
            public void onCompleted() {
                Toast.makeText(PullRefreshActivity.this, getResources().getString(R.string
                        .mei_refresh_success), Toast.LENGTH_SHORT).show();
                PullRefreshActivity.this.setLoadingMore(false);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anim:
                final PopupMenu popupMenu = new PopupMenu(this, getToolbarView(), GravityCompat
                        .END);
                popupMenu.inflate(R.menu.refresh_type);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dingdang_type:
                                setRefreshHeader(new DingDangHeader(PullRefreshActivity.this));
                                break;
                            case R.id.bezier_type:
                                setRefreshHeader(new BezierRadarHeader(PullRefreshActivity.this));
                                break;
                            case R.id.classics_type:
                                setRefreshHeader(new ClassicsHeader(PullRefreshActivity.this));
                                break;
                        }
                        autoRefresh();
                        popupMenu.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }
        return true;
    }
}
