package com.wsy.testuiapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wsy.testuiapplication.base.BasePresenter;
import com.wsy.testuiapplication.util.ActManageUtil;
import com.wsy.testuiapplication.util.AppUtil;
import com.wsy.testuiapplication.util.Slog;
import com.wsy.testuiapplication.util.StatusBarUtil;

/**
 * Created by qwl on 2017/8/28.
 */

public class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T mPresenter;
    protected boolean mJumpTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Slog.d("BaseActivity--" + getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        ActManageUtil.registerActivity(this);
        StatusBarUtil.setStatusBarTranslucent(this, true);
    }

    @Override
    public void finish() {
        Slog.d("BaseActivity--" + getClass().getSimpleName(), "finish");
        if (mJumpTag && ActManageUtil.getActivitySize() == 1) {
            AppUtil.startApp(this, getPackageName());
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        Slog.d("BaseActivity--" + getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
        ActManageUtil.unregisterActivity(this);
    }
}
