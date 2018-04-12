package com.meis.basemodule.activity;

import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.meis.base.mei.BaseActivity;
import com.meis.base.mei.ViewState;
import com.meis.basemodule.R;

/**
 * author: ws4
 * created on: 2018/4/11 15:31
 * description:
 */
public class StatusActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getToolbarView().setTitle(getResources().getString(R.string.status_type));
        getToolbarView().setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getToolbarView().inflateMenu(R.menu.type);
        getToolbarView().setOnMenuItemClickListener(this);
        getToolbarView().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setState(ViewState.EMPTY);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anim:
                final PopupMenu popupMenu = new PopupMenu(this, getToolbarView(), GravityCompat
                        .END);
                popupMenu.inflate(R.menu.custime_type);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.empty_type:
                                setState(ViewState.EMPTY, new Object[]{R.mipmap.ic_empty_page,
                                        getResources().getString(R.string.empty_type_1)});
                                break;
                            case R.id.error_type:
                                setState(ViewState.ERROR);
                                break;
                            case R.id.loading_type:
                                setState(ViewState.LOADING);
                                break;
                        }
                        popupMenu.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }
        return true;
    }

    @Override
    public void onErrorRetry() {
        super.onErrorRetry();
        Toast.makeText(this, getResources().getString(R.string.reload_data), Toast.LENGTH_SHORT)
                .show();
    }
}
