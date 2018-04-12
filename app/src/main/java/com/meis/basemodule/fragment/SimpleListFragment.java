package com.meis.basemodule.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.fragment.BaseListFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.MeiSimpleAdapter;
import com.meis.basemodule.entity.Article;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * author: ws4
 * created on: 2018/4/11 17:35
 * description:
 */
public class SimpleListFragment extends BaseListFragment<Article> {

    RecyclerView mRecyclerView;
    MeiSimpleAdapter mAdapter;

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected BaseAdapter<Article> getAdapter() {
        return mAdapter = new MeiSimpleAdapter();
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
            Article article = new Article(getResources().getStringArray(R.array.array_title)
                    [index], getResources().getStringArray(R.array.array_content)[index]);
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
