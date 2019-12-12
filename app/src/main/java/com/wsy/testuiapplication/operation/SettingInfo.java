package com.wsy.testuiapplication.operation;

import android.content.Context;
import android.content.SharedPreferences;

import com.wsy.testuiapplication.DFApplication;

/**
 * Created by qinmin on 2017/8/30.
 */
public class SettingInfo {

    private static final String PREF_NAME = "user_setting";
    private static final String PREF_USERID = "mUserId";//用户id
    private static final String PREF_REMIND_ONCE = "play_remind_once";//播放和下载提示


    public int mUserId;
    public boolean mPlayRemindOnce;
    /*是否提醒过*/
    public boolean mIsReminded;

    private static SettingInfo mInstance;

    private SettingInfo() {
    }

    public static SettingInfo getInstance() {
        if (mInstance == null) {
            mInstance = new SettingInfo();
        }
        return mInstance;
    }

    public void setmIsReminded(boolean mIsReminded) {
        this.mIsReminded = mIsReminded;
    }

    public boolean ismIsReminded() {
        return mIsReminded;
    }

    public void load() {
        Context sContext = DFApplication.getInstance().getApplicationContext();
        SharedPreferences sPreferences = sContext.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);

        mUserId = sPreferences.getInt(PREF_USERID, 0);
        mPlayRemindOnce = sPreferences.getBoolean(PREF_REMIND_ONCE, false);
    }

    public void save() {
        Context sContext = DFApplication.getInstance().getApplicationContext();
        SharedPreferences sPreferences = sContext.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor sEditor = sPreferences.edit();

        sEditor.putInt(PREF_USERID, mUserId);
        sEditor.putBoolean(PREF_REMIND_ONCE, mPlayRemindOnce);

        sEditor.commit();
    }
}
