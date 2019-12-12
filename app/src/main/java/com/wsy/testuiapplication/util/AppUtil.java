package com.wsy.testuiapplication.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.wsy.testuiapplication.DFApplication;

import java.io.File;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by qwl on 2017/8/28.
 */

public class AppUtil {

    private static final String TAG = "AppUtil";

    /**
     * Return the version name of the package.
     *
     * @return Version name of the package.
     */
    public static String getAppVersionName() {
        try {
            return DFApplication.getInstance().getApplicationContext().getPackageManager()
                    .getPackageInfo(
                            DFApplication.getInstance().getApplicationContext().getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the versionCode of the package.
     *
     * @return version code of the package.
     */
    public static int getAppVersionCode() {
        try {
            int code;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                code = (int) DFApplication.getInstance().getApplicationContext().getPackageManager()
                        .getPackageInfo(
                                DFApplication.getInstance().getApplicationContext()
                                        .getPackageName(),
                                0).getLongVersionCode();
            } else {
                code = DFApplication.getInstance().getApplicationContext().getPackageManager()
                        .getPackageInfo(
                                DFApplication.getInstance().getApplicationContext()
                                        .getPackageName(),
                                0).versionCode;
            }
            return code;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Check if the package exists.
     *
     * @param context Current context.
     * @param pkgName Target package name.
     * @return whether app exist
     */
    public static boolean appExists(Context context, String pkgName) {
        if (StringUtil.isEmpty(pkgName)) {
            return false;
        }

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> appsList = packageManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : appsList) {
            if (info.activityInfo.packageName.equals(pkgName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Start another app by package name.
     *
     * @param context Current context.
     * @param pkgName Package name of the target app.
     * @return true: succeeded, false: failed.
     */
    public static boolean startApp(Context context, String pkgName) {
        if (!appExists(context, pkgName)) {
            Log.e(TAG, "Package name is empty.");
            return false;
        }
        Intent intent = new Intent(context.getPackageManager().getLaunchIntentForPackage(pkgName));
        context.startActivity(intent);
        return true;
    }

    /**
     * Start activity of another app by package name and activity name.
     *
     * @param context  Current context.
     * @param pkgName  Package name of the app.
     * @param activity Target activity name.
     * @return true: succeeded, false: failed.
     */
    public static boolean startActivity(Context context, String pkgName, String activity) {
        if (!appExists(context, pkgName)) {
            Log.e(TAG, "Package name is empty.");
            return false;
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName(pkgName, activity);
        intent.setComponent(comp);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * Start a service.
     *
     * @param context Current context.
     * @param pkgName Target service package name.
     * @return
     */
    public static boolean startService(Context context, String pkgName) {
        if (!appExists(context, pkgName)) {
            Log.e(TAG, "Package name is empty.");
            return false;
        }
        Intent intent = new Intent(context.getPackageManager().getLaunchIntentForPackage(pkgName));
        context.startService(intent);
        return true;
    }

    /**
     * install one application
     *
     * @param context the context
     * @param apkFile the apk file
     */
    public static void installApplication(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Whether to open the notification button above API-19
     *
     * @param context context
     * @return whether notification enable
     */
    public static boolean isNotificationEnable(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * get UUID
     *
     * @return String
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * get UniqueID just once changed next time
     *
     * @return unique id
     */
//    public static String getUniqueId() {
//        Context context = DFApplication.getInstance().getApplicationContext();
//        if (!TextUtils.isEmpty(PreferencesUtil.getString(context, AppConstants.UNIQUE_ID))) {
//            return PreferencesUtil.getString(context, AppConstants.UNIQUE_ID);
//        }
//        String androidID = Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//        String id = androidID + Build.SERIAL;
//        try {
//            String mixID = toMD5(id + getUUID());
//            PreferencesUtil.putString(context, AppConstants.UNIQUE_ID, mixID);
//            return mixID;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            PreferencesUtil.putString(context, AppConstants.UNIQUE_ID, id);
//            return id;
//        }
//    }

    /**
     * md5
     *
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String toMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] digest = messageDigest.digest(text.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            int digestInt = digest[i] & 0xff;
            String hexString = Integer.toHexString(digestInt);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    /**
     * get the client id of the device
     *
     * @return client id
     */
//    public static String getClientId() {
//        Context context = DFApplication.getInstance().getApplicationContext();
//        if (!TextUtils.isEmpty(PreferencesUtil.getString(context, AppConstants.CLIENT_ID))) {
//            return PreferencesUtil.getString(context, AppConstants.CLIENT_ID);
//        }
//        String clientId = getUUID();
//        PreferencesUtil.putString(context, AppConstants.CLIENT_ID, clientId);
//        return clientId;
//    }

    public enum DeviceInformation {
        /**
         * The mac-address of the device.
         */
        DEVICE_INFO_MAC_ADDRESS,
        /**
         * The user-visible version string.
         */
        DEVICE_INFO_VERSION_RELEASE,
        /**
         * The manufacturer of the product/hardware.
         */
        DEVICE_INFO_BUILD_MANUFACTURER,
        /**
         * The end-user-visible name for the end product.
         */
        DEVICE_INFO_BUILD_MODEL
    }

    /**
     * get information about hardware devices
     *
     * @param informationType {@link DeviceInformation#DEVICE_INFO_MAC_ADDRESS},
     *                        {@link DeviceInformation#DEVICE_INFO_VERSION_RELEASE},
     *                        {@link DeviceInformation#DEVICE_INFO_BUILD_MANUFACTURER},
     *                        {@link DeviceInformation#DEVICE_INFO_BUILD_MODEL}
     * @return
     */
    public static String getDeviceInformation(DeviceInformation informationType) {
        switch (informationType) {
            case DEVICE_INFO_MAC_ADDRESS:
                try {
                    List<NetworkInterface> all = Collections
                            .list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface nif : all) {
                        if (!nif.getName().equalsIgnoreCase("wlan0"))
                            continue;
                        byte[] macBytes = nif.getHardwareAddress();
                        if (macBytes == null) {
                            return "";
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (byte b : macBytes) {
                            stringBuilder.append(String.format("%02X:", b));
                        }
                        if (stringBuilder.length() > 0) {
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        }
                        return stringBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            case DEVICE_INFO_VERSION_RELEASE:
                return "Android " + Build.VERSION.RELEASE;
            case DEVICE_INFO_BUILD_MANUFACTURER:
                return Build.MANUFACTURER;
            case DEVICE_INFO_BUILD_MODEL:
                return Build.MODEL;
            default:
                return "MissingParameters";
        }
    }

}
