package com.meis.base.mei.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: ws4
 * created on: 2018/4/11 14:12
 * description:
 */
public abstract class BaseDialog extends CompatDialog {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    public <T extends View> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
