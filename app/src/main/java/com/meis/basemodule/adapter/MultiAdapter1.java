package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.ItemPresenter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.MultiBean1;

/**
 * author: ws4
 * created on: 2018/4/12 15:18
 * description:
 */
public class MultiAdapter1 extends ItemPresenter<MultiBean1> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_image_view;
    }

    @Override
    public void convert(BaseViewHolder holder, MultiBean1 item) {

    }
}
