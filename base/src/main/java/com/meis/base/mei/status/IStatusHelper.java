package com.meis.base.mei.status;

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
    void onSetContentView(int layoutId);

    /**
     * error view retry loading
     */
    void onErrorRetry();
}
