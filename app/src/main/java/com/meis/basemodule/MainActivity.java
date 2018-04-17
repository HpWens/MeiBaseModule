package com.meis.basemodule;


import android.content.Intent;
import android.view.View;

import com.meis.base.mei.MeiBaseActivity;

public class MainActivity extends MeiBaseActivity {

    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        getToolbarView().setTitle("BaseModule");
    }

    public void clickActivity(View view) {
        startActivity(new Intent(this, com.meis.basemodule.activity.MainActivity.class));
    }

    public void clickFragment(View view) {
        startActivity(new Intent(this, com.meis.basemodule.fragment.MainActivity.class));
    }

    public void clickDialog(View view) {
        startActivity(new Intent(this, com.meis.basemodule.dialog.MainActivity.class));
    }
}
