package com.meis.basemodule.activity;

import android.content.Intent;
import android.view.View;

import com.meis.base.mei.BaseActivity;
import com.meis.basemodule.R;
import com.meis.basemodule.activity.ui.CustomEmptyActivity;
import com.meis.basemodule.activity.ui.MultiListActivity;
import com.meis.basemodule.activity.ui.PullRefreshActivity;
import com.meis.basemodule.activity.ui.SingleListActivity;
import com.meis.basemodule.activity.ui.StatusActivity;
import com.meis.basemodule.activity.ui.TranslucentActivity;

/**
 * author: ws4
 * created on: 2018/4/11 15:04
 * description:
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.activity_module));
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_module;
    }

    public void clickStatus(View view) {
        startActivity(new Intent(this, StatusActivity.class));
    }

    public void clickCusStatus(View view) {
        startActivity(new Intent(this, CustomEmptyActivity.class));
    }

    public void clickPullRefresh(View view) {
        startActivity(new Intent(this, PullRefreshActivity.class));
    }

    public void clickSingleList(View view) {
        startActivity(new Intent(this, SingleListActivity.class));
    }

    public void clickMultiList(View view) {
        startActivity(new Intent(this, MultiListActivity.class));
    }

    public void clickTranslucent(View view) {
        startActivity(new Intent(this, TranslucentActivity.class));
    }
}
