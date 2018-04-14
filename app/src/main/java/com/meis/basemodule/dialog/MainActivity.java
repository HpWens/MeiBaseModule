package com.meis.basemodule.dialog;

import android.view.View;

import com.meis.base.mei.BaseActivity;
import com.meis.basemodule.R;

/**
 * author: ws4
 * created on: 2018/4/11 15:06
 * description:
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getString(R.string.dialog_module));
        showDialog(new CreateLiveDialog());
    }

    public void clickLive(View v) {
        showDialog(new CreateLiveDialog());
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_dialog;
    }
}
