package com.wsy.testuiapplication.base;

/**
 * Created by qwl on 2017/9/28.
 */

public interface BasePresenter<T> {
    void attachView(T view);

    void detachView();

    void requestData();

    void destroy();
}
