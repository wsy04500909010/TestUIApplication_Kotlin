package com.wsy.testuiapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qwl on 2017/9/4.
 */

public class PreferencesUtil {

    private static final String PREFERENCES_NAME_DFTV = "dragonfly";

    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_DFTV,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
