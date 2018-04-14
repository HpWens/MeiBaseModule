package com.meis.base.mei.utils;

import java.util.Collection;

/**
 * author: ws4
 * created on: 2018/4/11 18:19
 * description:集合工具
 */
public class ListUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
}
