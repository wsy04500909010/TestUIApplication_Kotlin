package com.wsy.testuiapplication.util;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class LanguageUtil {

    //language from system
    public static final String LANGUAGE_ZH = "zh";
    public static final String LANGUAGE_EN = "en";
    public static final String COUNTRY_TW = "TW";
    public static final String COUNTRY_CN = "CN";

    public static final int LANGUAGE_ZHCN = 1;
    public static final int LANGUAGE_ZHTW = 2;
    public static final int LANGUAGE_ENUS = 3;

    public static Locale getLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }
}
