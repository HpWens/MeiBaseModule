package com.meis.basemodule.fragment.ui.third;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.fragment.BaseFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.ChatRightAdapter;
import com.meis.basemodule.entity.Chat;

/**
 * author: ws4
 * created on: 2018/4/13 11:18
 * description:
 */
public class MsgFragment extends BaseFragment {

    private static final String ARG_MSG = "arg_msg";

    private Chat mChat;

    private RecyclerView mRecyclerView;

    private ChatRightAdapter mAdapter;

    private EditText mEditText;

    public static MsgFragment newInstance(Chat msg) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        MsgFragment fragment = new MsgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_MSG)) {
                this.mChat = args.getParcelable(ARG_MSG);
            }
        }

        mRecyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(mChat.name);
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter = new ChatRightAdapter());

        mAdapter.addData(mChat);

        mEditText = findViewById(R.id.et_send);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mEditText.getText().toString().trim();
                if (!content.equals("")) {
                    Chat chat = new Chat();
                    chat.id = content.hashCode();
                    chat.message = content;
                    mAdapter.addData(chat);
                    mEditText.setText("");
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mActivity, getString(R.string.chat_header_click), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_tab_first_msg;
    }
}
