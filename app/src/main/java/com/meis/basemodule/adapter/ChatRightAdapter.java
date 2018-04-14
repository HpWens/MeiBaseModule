package com.meis.basemodule.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.Chat;

/**
 * desc:
 * author: ws
 * date: 2018/4/13.
 */

public class ChatRightAdapter extends BaseAdapter<Chat> {

    public ChatRightAdapter() {
        super(R.layout.item_zhihu_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, Chat item) {
        helper.setText(R.id.tv_msg, item.message)
                .addOnClickListener(R.id.img_avatar);
    }
}
