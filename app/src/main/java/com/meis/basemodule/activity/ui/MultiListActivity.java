package com.meis.basemodule.activity.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meis.base.mei.BaseActivity;
import com.meis.base.mei.BaseListActivity;
import com.meis.base.mei.adapter.BaseAdapter;
import com.meis.base.mei.adapter.BaseMixAdapter;
import com.meis.base.mei.entity.Result;
import com.meis.basemodule.R;
import com.meis.basemodule.adapter.MultiAdapter;
import com.meis.basemodule.adapter.MultiAdapter1;
import com.meis.basemodule.adapter.MultiAdapter2;
import com.meis.basemodule.adapter.MultiAdapter3;
import com.meis.basemodule.entity.Article;
import com.meis.basemodule.entity.MultiBean;
import com.meis.basemodule.entity.MultiBean1;
import com.meis.basemodule.entity.MultiBean2;
import com.meis.basemodule.entity.MultiBean3;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * author: ws4
 * created on: 2018/4/11 15:33
 * description:
 */
public class MultiListActivity extends BaseListActivity<Object> {

    BaseMixAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void initData() {
        super.initData();

        getToolbarView().setTitle(getResources().getString(R.string.mul_type));
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                Object o = mAdapter.getItem(position);
                if (o instanceof MultiBean1) {
                    return 1;
                } else if (o instanceof MultiBean2) {
                    return 4;
                } else if (o instanceof MultiBean) {
                    return 2;
                }
                return 3;
            }
        });
        return mRecyclerView;
    }

    @Override
    protected BaseAdapter<Object> getAdapter() {
        mAdapter = new BaseMixAdapter();
        mAdapter.addItemPresenter(new MultiAdapter1());
        mAdapter.addItemPresenter(new MultiAdapter2());
        mAdapter.addItemPresenter(new MultiAdapter3());
        mAdapter.addItemPresenter(new MultiAdapter());
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<Object>>> getListObservable(int pageNo) {
        return getData(pageNo);
    }

    public Observable<Result<List<Object>>> getData(int pageNo) {
        Result<List<Object>> result = new Result<>();
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            objectList.add(new MultiBean1());
            objectList.add(new MultiBean3());
            objectList.add(new MultiBean2());
            objectList.add(new MultiBean());
            objectList.add(new MultiBean());
        }
        result.data = objectList;
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
    protected int layoutResId() {
        return R.layout.comm_recycler;
    }
}
