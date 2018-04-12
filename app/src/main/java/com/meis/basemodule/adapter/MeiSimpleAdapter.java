package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.Article;

/**
 * author: ws4
 * created on: 2018/4/11 17:40
 * description:
 */
public class MeiSimpleAdapter extends BaseAdapter<Article> {

    public MeiSimpleAdapter() {
        super(R.layout.item_comm);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
    }
}
