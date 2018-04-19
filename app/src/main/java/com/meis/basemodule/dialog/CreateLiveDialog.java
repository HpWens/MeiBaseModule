package com.meis.basemodule.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.meis.basemodule.R;
import com.meis.basemodule.base.BaseDialog;

/**
 * desc:
 * author: ws
 * date: 2018/4/14.
 */

public class CreateLiveDialog extends BaseDialog {

    TextView mTvLive;
    TextView mTvVideo;
    ImageView mIvClose;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.CreateLiveDialog);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_live;
    }

    @Override
    protected void initView() {
        mTvLive = findViewById(R.id.create_live_tv);
        mTvVideo = findViewById(R.id.create_shortvideo_tv);
        mIvClose = findViewById(R.id.close_iv);
    }

    @Override
    protected void initData() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.live_btn_bottom_in);
        animation.setStartOffset(80);
        mTvLive.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.live_btn_bottom_in);
        animation.setStartOffset(160);
        mTvVideo.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.live_btn_bottom_in);
        animation.setStartOffset(200);
        mIvClose.startAnimation(animation);

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
