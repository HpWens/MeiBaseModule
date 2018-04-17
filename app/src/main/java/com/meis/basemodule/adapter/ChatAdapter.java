package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.Chat;

/**
 * author: ws4
 * created on: 2018/4/12 18:00
 * description:
 */
public class ChatAdapter extends MeiBaseAdapter<Chat> {

    public ChatAdapter() {
        super(R.layout.item_zhihu_chat);
    }

    @Override
    protected void convert(BaseViewHolder helper, Chat item) {
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_msg, item.message);

    }
}
