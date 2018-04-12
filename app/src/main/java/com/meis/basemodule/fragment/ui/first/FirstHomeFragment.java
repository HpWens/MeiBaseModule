package com.meis.basemodule.fragment.ui.first;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.fragment.BaseListFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.FirstHomeAdapter;
import com.meis.basemodule.adapter.MeiSimpleAdapter;
import com.meis.basemodule.entity.Article;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class FirstHomeFragment extends BaseListFragment<Article> {

    private String[] mTitles = new String[]{
            "Use imagery to express a distinctive voice and exemplify creative excellence.",
            "An image that tells a story is infinitely more interesting and informative.",
            "The most powerful iconic images consist of a few meaningful elements, with minimal " +
                    "distractions.",
            "Properly contextualized concepts convey your message and brand more effectively.",
            "Have an iconic point of focus in your imagery. Focus ranges from a single entity to " +
                    "an overarching composition."
    };

    @Override
    protected void initData() {
        super.initData();
        getToolbarView().setTitle(getResources().getString(R.string.home));
    }

    private int[] mImgRes = new int[]{
            R.mipmap.bg_first, R.mipmap.bg_second, R.mipmap.bg_third, R.mipmap.bg_fourth, R
            .mipmap.bg_fifth
    };


    public static FirstHomeFragment newInstance() {

        Bundle args = new Bundle();

        FirstHomeFragment fragment = new FirstHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    protected BaseAdapter<Article> getAdapter() {
        FirstHomeAdapter adapter = new FirstHomeAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FirstDetailFragment fragment = FirstDetailFragment.newInstance(mAdapter.getItem
                        (position));
                start(fragment);
            }
        });
        return adapter;
    }

    @Override
    protected Observable<Result<List<Article>>> getListObservable(int pageNo) {
        return getData(pageNo);
    }

    public Observable<Result<List<Article>>> getData(int pageNo) {
        Result<List<Article>> result = new Result<>();
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * 3);
            Article article = new Article(mTitles[index], mImgRes[index]);
            articleList.add(article);
        }
        result.data = articleList;
        result.resultCode = 0;
        result.message = "";
        return Observable.just(result);
    }

    @Override
    protected boolean canLoadMore() {
        return true;
    }

    @Override
    protected boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

}
