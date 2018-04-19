package com.meis.basemodule.activity.ui;

import android.view.View;

import com.meis.base.mei.status.ViewState;
import com.meis.basemodule.R;
import com.meis.basemodule.base.BaseActivity;

/**
 * author: ws4
 * created on: 2018/4/11 15:31
 * description:
 */
public class CustomEmptyActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.cus_status_type));
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // setEmptyLayout 在 setState 方法之前调用
        setEmptyLayout(R.layout.activity_custom_base_empty);
        setState(ViewState.EMPTY);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
