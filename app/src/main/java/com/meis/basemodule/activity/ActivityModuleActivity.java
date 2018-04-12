package com.meis.basemodule.activity;

import android.content.Intent;
import android.view.View;

import com.meis.base.mei.BaseActivity;
import com.meis.basemodule.R;

/**
 * author: ws4
 * created on: 2018/4/11 15:04
 * description:
 */
public class ActivityModuleActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.activity_module));
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_module_activity;
    }

    public void clickStatus(View view) {
        startActivity(new Intent(this, StatusActivity.class));
    }

    public void clickCusStatus(View view) {
        startActivity(new Intent(this, CusStatusActivity.class));
    }

    public void clickRefresh(View view) {
        startActivity(new Intent(this, RefreshActivity.class));
    }

    public void clickSimple(View view) {
        startActivity(new Intent(this, SimpleListActivity.class));
    }

    public void clickMulti(View view) {
        startActivity(new Intent(this, MulListActivity.class));
    }
}
