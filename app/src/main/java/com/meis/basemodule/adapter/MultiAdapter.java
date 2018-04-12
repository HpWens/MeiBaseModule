package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.ItemPresenter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.MultiBean;

/**
 * author: ws4
 * created on: 2018/4/12 15:38
 * description:
 */
public class MultiAdapter extends ItemPresenter<MultiBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_section_content;
    }

    @Override
    public void convert(BaseViewHolder holder, MultiBean item) {

    }
}
