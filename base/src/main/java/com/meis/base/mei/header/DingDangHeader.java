package com.meis.base.mei.header;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meis.base.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * author: ws4
 * created on: 2018/4/9 19:53
 * description:丁丁当头部下拉样式
 */
public class DingDangHeader extends LinearLayout implements RefreshHeader {

    private Context mContent;
    private ImageView mIvDingDang;
    private TextView mTvStatus;

    private boolean mIsRefreshing = false;

    private LoadingAnimation mLoadingAnimation;
    //总等级数
    private static final int TOTAL_LEVEL = 35;
    //最大等级
    private static final float MAX_STATUS_LEVEL = 16;
    //等级发生改变的高度
    private static final int LEVEL_CHANGE_HEIGHT = 120;

    private static final int FIXED_PARAMS_VALUE = 40;

    public DingDangHeader(Context context) {
        this(context, null);
    }

    public DingDangHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DingDangHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        this.mContent = context;
        View view = LayoutInflater.from(mContent).inflate(R.layout.mei_dingdang_refresh_header, null);
        mIvDingDang = view.findViewById(R.id.refresh_image);
        mTvStatus = view.findViewById(R.id.refresh_text);
        addView(view);
        setGravity(Gravity.CENTER);
        setMinimumHeight(DensityUtil.dp2px(64));
    }

    /**
     * 获取真实视图（必须返回，不能为null）
     */
    @NonNull
    @Override
    public View getView() {
        return this;
    }

    /**
     * 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）
     */
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     *
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    @Override
    public void setPrimaryColors(int... colors) {

    }

    /**
     * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     *
     * @param kernel       RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height       HeaderHeight or FooterHeight
     * @param extendHeight 最大拖动高度
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    /**
     * 手指拖动下拉（会连续多次调用，添加isDragging并取代之前的onPulling、onReleasing）
     *
     * @param isDragging   true 手指正在拖动 false 回弹动画
     * @param percent      下拉的百分比 值 = offset/footerHeight (0 - percent -
     *                     (footerHeight+extendHeight) / footerHeight )
     * @param offset       下拉的像素偏移量  0 - offset - (footerHeight+extendHeight)
     * @param height       高度 HeaderHeight or FooterHeight
     * @param extendHeight 扩展高度  extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int
            extendHeight) {
        if (!mIsRefreshing) {
            mIvDingDang.setImageLevel(getLevel(offset));
        }
    }

    /**
     * 释放时刻（调用一次，将会触发加载）
     *
     * @param refreshLayout RefreshLayout
     * @param height        高度 HeaderHeight or FooterHeight
     * @param extendHeight  扩展高度  extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     *
     * @param refreshLayout RefreshLayout
     * @param height        HeaderHeight or FooterHeight
     * @param extendHeight  最大拖动高度
     */
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int
            extendHeight) {
        mIsRefreshing = true;
        startLoadingAnimation();
    }

    /**
     * 动画结束
     *
     * @param refreshLayout RefreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mIsRefreshing = false;
        oneshotLoadingAnimation();
        mTvStatus.setText(getResources().getString(success ? R.string.mei_refresh_success : R
                .string.mei_refresh_failure));
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState
            oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mTvStatus.setText(getResources().getString(R.string.mei_pull_refresh));
                break;
            case Refreshing:
                mTvStatus.setText(getResources().getString(R.string.mei_data_updating));
                break;
            case ReleaseToRefresh:
                mTvStatus.setText(getResources().getString(R.string.mei_release_refresh));
                break;
        }
    }

    private int getLevel(int moveHeight) {
        return (int) Math.max(0, Math.min(MAX_STATUS_LEVEL / 2, (float) (moveHeight -
                LEVEL_CHANGE_HEIGHT) / moveHeight * FIXED_PARAMS_VALUE / 2));
    }

    private void oneshotLoadingAnimation() {
        LoadingAnimation animation = mLoadingAnimation;
        if (animation != null) {
            animation.oneshot = true;
        }
    }

    private void startLoadingAnimation() {
        stopLoadingAnimation();
        mLoadingAnimation = new LoadingAnimation(mIvDingDang.getDrawable().getLevel(), false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mIvDingDang.postOnAnimation(mLoadingAnimation);
        }
    }

    private void stopLoadingAnimation() {
        if (mLoadingAnimation != null) {
            mIvDingDang.removeCallbacks(mLoadingAnimation);
        }
        mLoadingAnimation = null;
    }

    private class LoadingAnimation implements Runnable {

        int level;
        boolean oneshot;

        public LoadingAnimation(int level, boolean oneshot) {
            this.level = level;
            this.oneshot = oneshot;
        }

        @Override
        public void run() {
            int nextLevel = level++ % TOTAL_LEVEL;
            mIvDingDang.setImageLevel(nextLevel);
            if (mLoadingAnimation != this) {
                return;
            } else if (oneshot && nextLevel == (TOTAL_LEVEL - 1)) {
                return;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mIvDingDang.postOnAnimationDelayed(this, 20);
                }
            }
        }
    }
}
