package com.meis.base.mei.rxjava;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author: ws4
 * created on: 2018/4/11 11:20
 * description: 防止正在刷新 home 键，再次返回 刷新不会停止
 */
public abstract class UiSubscriber<T> implements Observer<T> {

    public abstract void onCompleted();

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        onCompleted();
    }
}
