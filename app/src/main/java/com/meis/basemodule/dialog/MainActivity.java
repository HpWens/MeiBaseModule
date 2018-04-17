package com.meis.basemodule.dialog;

import android.view.View;

import com.meis.base.mei.MeiBaseActivity;
import com.meis.basemodule.R;

/**
 * author: ws4
 * created on: 2018/4/11 15:06
 * description:
 */
public class MainActivity extends MeiBaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getString(R.string.dialog_module));
        showDialog(new CreateLiveDialog());
    }

    public void clickLive(View view) {
        showDialog(new CreateLiveDialog());
    }

    public void clickComment(View view) {
        showDialog(new BottomCommentDialog());
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_dialog;
    }
}
