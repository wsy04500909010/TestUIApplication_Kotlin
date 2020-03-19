package com.wsy.testuiapplication.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by qwl on 2018/4/16.
 */
public class ActManageUtil {

    private static Set<Activity> sAllActivities;

    public static void registerActivity(Activity activity) {
        if (sAllActivities == null) {
            sAllActivities = new HashSet<>();
        }
        sAllActivities.add(activity);
    }

    public static void unregisterActivity(Activity activity) {
        if (sAllActivities != null) {
            sAllActivities.remove(activity);
        }
    }

    public static int getActivitySize() {
        if (sAllActivities != null) {
            return sAllActivities.size();
        }
        return 0;
    }

    public static Activity getTopActivity() {
        if (sAllActivities != null) {
            return sAllActivities.iterator().next();
        }
        return null;
    }

    public static void exitApp() {
        if (sAllActivities != null) {
            synchronized (sAllActivities) {
                for (Activity act : sAllActivities) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private static List<Activity> aList = new ArrayList<>();

    public static void addActivity(Activity activity){
        aList.add(activity);
    }
    public static void removeActivity(Activity activity){
        aList.remove(activity);
    }

//    public static int getActivitySize(){
//        return aList.size();
//    }

    public static void exit(){
        for (Activity activity : aList) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
