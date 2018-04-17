package com.meis.basemodule.activity.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meis.base.mei.MeiBaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.meis.basemodule.R;

/**
 * desc:
 * author: ws
 * date: 2018/4/14.
 */

public class TranslucentActivity extends MeiBaseActivity {

    Toolbar mToolbar;

    @Override
    protected void initView() {
        //调用Eyes设置各种透明状态
        Eyes.translucentStatusBar(this, true);
        mToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void initData() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_translucent;
    }
}
