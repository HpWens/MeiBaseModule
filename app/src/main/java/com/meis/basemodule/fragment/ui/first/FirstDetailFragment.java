package com.meis.basemodule.fragment.ui.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meis.base.mei.fragment.BaseFragment;
import com.meis.basemodule.R;
import com.meis.basemodule.entity.Article;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class FirstDetailFragment extends BaseFragment {

    private static final String ARG_ITEM = "arg_item";

    private Article mArticle;

    private ImageView mImgDetail;
    private TextView mTvTitle;
    private Toolbar mToolbar;

    public static FirstDetailFragment newInstance(Article article) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, article);
        FirstDetailFragment fragment = new FirstDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticle = getArguments().getParcelable(ARG_ITEM);
    }

    @Override
    protected void initView() {
        mImgDetail = (ImageView) findViewById(R.id.img_detail);
        mTvTitle = (TextView) findViewById(R.id.tv_content);
        mToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void initData() {
        mImgDetail.setImageResource(mArticle.getImgRes());
        mTvTitle.setText(mArticle.getTitle());
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_first_detail;
    }
}
