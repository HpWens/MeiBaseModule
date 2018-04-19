package com.meis.basemodule.fragment.ui.third;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.header.DingDangHeader;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.ChatAdapter;
import com.meis.basemodule.base.BaseListFragment;
import com.meis.basemodule.entity.Chat;
import com.meis.basemodule.entity.Result;
import com.meis.basemodule.fragment.MainFragment;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuThirdFragment extends BaseListFragment<Chat> {

    public static ZhihuThirdFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuThirdFragment fragment = new ZhihuThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.message));
    }

    @Override
    protected RecyclerView getRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        return recyclerView;
    }

    @Override
    protected MeiBaseAdapter<Chat> getAdapter() {
        ChatAdapter chatAdapter = new ChatAdapter();
        chatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()

                // 也可以像使用getParentFragment()的方式,拿到父Fragment来操作 或者使用 EventBusActivityScope
                ((MainFragment) getParentFragment()).start(MsgFragment.newInstance(mAdapter
                        .getItem(position)));
            }
        });
        return chatAdapter;
    }

    @Override
    protected Observable<Result<List<Chat>>> getListObservable(int pageNo) {
        return getData(pageNo);
    }

    public Observable<Result<List<Chat>>> getData(int pageNo) {
        Result<List<Chat>> result = new Result<>();
        List<Chat> chatList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Chat chat = new Chat();
            chat.id = i;
            String[] name = new String[]{"Jake", "Eric", "Kenny", "Helen", "Carr"};
            String[] chats = new String[]{"message1", "message2", "message3", "message4",
                    "message5"};
            chat.name = name[i % name.length];
            chat.message = chats[i % name.length];
            chatList.add(chat);
        }
        result.data = chatList;
        result.resultCode = 0;
        result.message = "";
        return Observable.just(result);
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return new DingDangHeader(mActivity);
    }

    @Override
    protected boolean loadOnShow() {
        return false;
    }
}
