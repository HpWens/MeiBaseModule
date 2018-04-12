package com.meis.basemodule.adapter;

import android.support.v4.view.ViewCompat;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.Article;

/**
 * author: ws4
 * created on: 2018/4/12 16:31
 * description:
 */
public class FirstHomeAdapter extends BaseAdapter<Article> {

    public FirstHomeAdapter() {
        super(R.layout.item_zhihu_home_first);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
        ViewCompat.setTransitionName(helper.getView(R.id.img), String.valueOf(helper
                .getAdapterPosition()) + "_image");
        ViewCompat.setTransitionName(helper.getView(R.id.tv_title), String.valueOf(helper
                .getAdapterPosition()) + "_tv");

        helper.setImageResource(R.id.img, item.getImgRes());
        helper.setText(R.id.tv_title, item.getTitle());
    }
}
