package com.wsy.testuiapplication;

import android.support.multidex.MultiDexApplication;


public class DFApplication extends MultiDexApplication {

    private static DFApplication mInstance;
    public static final double VOLUME_INCREMENT = 0.05;

    /**
     * Get the singleton application instance.
     *
     * @return DFApplication instance.
     */
    public static DFApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        DFData.getInstance().init(this);
//        SettingInfo.getInstance().load();
//        UserController.getInstance().init();
//        DbUtils.getInstance().deleteAllOutOfSeekData();
        initVideoCastManager();
    }

    private void initVideoCastManager() {
        String applicationId = "584C20C6";

        // initialize VideoCastManager
//        VideoCastManager.
//                initialize(this, applicationId, null, null).
//                setVolumeStep(VOLUME_INCREMENT).
//                enableFeatures(VideoCastManager.FEATURE_NOTIFICATION |
//                        VideoCastManager.FEATURE_LOCKSCREEN |
//                        VideoCastManager.FEATURE_WIFI_RECONNECT |
//                        VideoCastManager.FEATURE_CAPTIONS_PREFERENCE |
//                        VideoCastManager.FEATURE_DEBUGGING);
    }
}
