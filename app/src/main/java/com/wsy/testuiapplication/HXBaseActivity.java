package com.wsy.testuiapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.wsy.testuiapplication.util.ActManageUtil;
import com.wsy.testuiapplication.util.Slog;

/**
 * Created by WSY on 2020/2/4.
 */
public class HXBaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    public static final int REQUEST_CODE_NET_ERROR = 1;

    private static HXBaseActivity mTopActivity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Slog.d(TAG,this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        ActManageUtil.addActivity(this);

        initView();
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTopActivity = this;
//        if (!NetworkUtil.isConnected(this)&& !HuanxinApp.isLauncherActivity(this)) {
//            startActivityForResult(new Intent(this, NetErrorActivity.class), REQUEST_CODE_NET_ERROR);
//        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        setIntent(null);
    }

    /**
     * Do resource initialization work.
     */
    protected void initView() {}

    /**
     * Load page data.
     */
    protected void loadData() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_NET_ERROR
//                && resultCode == NetErrorActivity.RESULT_CODE_NET_CONNECTED) {
//            loadData();
//        }
    }

    public static HXBaseActivity getCurrentActivity() {
        return mTopActivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActManageUtil.removeActivity(this);
    }
}
