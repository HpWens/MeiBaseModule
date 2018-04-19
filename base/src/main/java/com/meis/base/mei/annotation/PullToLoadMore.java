package com.meis.base.mei.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: ws4
 * created on: 2018/4/8 13:56
 * description: 一行代码实现加载更多
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PullToLoadMore {
}
