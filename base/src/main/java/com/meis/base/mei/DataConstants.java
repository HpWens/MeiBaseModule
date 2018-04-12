package com.meis.base.mei;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface DataConstants {

    /**
     * 默认每页数量
     */
    int DEFAULT_PAGE_SIZE = 20;

    int MAX_PAGE_SIZE = 1024;

    /**
     * 接口的第一页索引
     */
    int FIRST_PAGE = 1;
}
