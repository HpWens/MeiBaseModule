package com.meis.base.mei.status;

import android.support.annotation.IntDef;

public interface ViewState {

    int REST = 0x01;
    int LOADING = 0x02;
    int REFRESH = 0x03;
    int COMPLETED = 0x04;
    int ERROR = 0x05;
    int EMPTY = 0x06;

    @IntDef({REST, LOADING, REFRESH, COMPLETED, ERROR, EMPTY})
    @interface Val {

    }
}
