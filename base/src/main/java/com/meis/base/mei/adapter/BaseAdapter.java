package com.meis.base.mei.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * author: ws4
 * created on: 2018/4/11 13:41
 * description:
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    /**
     * 当前页码
     */
    private int mPageCount;

    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseAdapter() {
        super(0);
    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
        mPageCount = 1;
    }

    @Override
    public void addData(@NonNull Collection<? extends T> newData) {
        super.addData(newData);
        mPageCount++;
    }

    public void clearData() {
        getData().clear();
        notifyDataSetChanged();
        mPageCount = 0;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getDataCount() {
        return getData().size();
    }
}
