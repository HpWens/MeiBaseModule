package com.meis.base.mei.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * desc: 软键盘工具
 * author: ws
 * date: 2018/4/13.
 */

public class SoftKeyboardUtils {
    public static void closeSoftKeyboard(Activity activity) {
        //隐藏软键盘
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
