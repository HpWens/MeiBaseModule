package com.meis.basemodule;


import android.content.Intent;
import android.view.View;

import com.meis.base.mei.BaseActivity;
import com.meis.basemodule.activity.ActivityModuleActivity;
import com.meis.basemodule.dialog.DialogModuleActivity;
import com.meis.basemodule.fragment.FragmentModuleActivity;

public class MainActivity extends BaseActivity {

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
        startActivity(new Intent(this, ActivityModuleActivity.class));
    }

    public void clickFragment(View view) {
        startActivity(new Intent(this, FragmentModuleActivity.class));
    }

    public void clickDialog(View view) {
        startActivity(new Intent(this, DialogModuleActivity.class));
    }
}
