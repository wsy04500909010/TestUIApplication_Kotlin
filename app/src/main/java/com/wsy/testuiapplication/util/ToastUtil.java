package com.wsy.testuiapplication.util;

import android.widget.Toast;

import com.wsy.testuiapplication.DFApplication;

/**
 * Created by zry on 2019/4/12.
 */
public class ToastUtil {

    private static Toast sToast;

    public static void showShortToast(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(DFApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void showShortToast(int msg) {
        if (sToast == null) {
            sToast = Toast.makeText(DFApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void showLongToast(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(DFApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_LONG);
        }
        sToast.show();
    }

    public static void showLongToast(int msg) {
        if (sToast == null) {
            sToast = Toast.makeText(DFApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_LONG);
        }
        sToast.show();
    }

}
