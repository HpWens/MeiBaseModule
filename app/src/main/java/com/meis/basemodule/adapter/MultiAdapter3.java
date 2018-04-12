package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.ItemPresenter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.MultiBean3;

/**
 * author: ws4
 * created on: 2018/4/12 15:20
 * description:
 */
public class MultiAdapter3 extends ItemPresenter<MultiBean3> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_text_view;
    }

    @Override
    public void convert(BaseViewHolder holder, MultiBean3 item) {

    }
}
