package com.meis.basemodule.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.meis.basemodule.R;
import com.meis.basemodule.base.BaseDialog;

/**
 * desc: 以底部弹出评论提示框 可以解决很多软键盘与滚动界面冲突问题
 * author: ws
 * date: 2018/4/14.
 */

public class BottomCommentDialog extends BaseDialog {

    @Override
    protected int getLayoutId() {
        return R.layout.comm_bottom_comment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 显示在底部
        params.gravity = Gravity.BOTTOM;
        // 宽度填充满屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //显示dialog的时候,就显示软键盘
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        window.setAttributes(params);

        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }
}
