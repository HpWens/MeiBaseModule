package com.meis.base.mei;

/**
 * author: ws4
 * created on: 2018/3/23 10:54
 * description:
 */
public interface IStatusHelper {

    /**
     * call activity super.setContentView(layoutId);
     *
     * @param layoutId
     */
    void onCallContentView(int layoutId);

    /**
     * error view retry loading
     */
    void onErrorRetry();
}
