package com.mirstone.sharelib.listener;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public interface CallBack<T> {
    void success(T t);
    void cancel();
    void failed(String error);
}
