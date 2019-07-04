package com.dming.simple.plugin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.util.HashMap;
import java.util.Map;

public class ActPlugin {

    static HashMap<String, String> sHostActMap = new HashMap<>();
    private static HashMap<String, ActivityInfo> sPluginActMap = new HashMap<>();
    private static final String PLUGIN_START_NAME = "com.dming.simple.Activity";
    public static String sPackageName;


    public static void startActivity(Activity activity, Intent intent, int requestCode, Bundle options) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = ActPitEvent.getInstance().startActivityForResult(intent, requestCode, options);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startActivity pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                activity.startActivityForResult(intent,requestCode,options);
            } else {
                SToast.showPluginError(activity);
            }
        } else {
            SToast.showPluginError(activity);
        }
    }

    public static String findActivityPit(String actName) {
        for (Map.Entry<String, String> entry : sHostActMap.entrySet()) {
            if (actName.equals(entry.getValue())) {  // 找到旧坑
                return entry.getKey();
            }
        }
        for (Map.Entry<String, String> entry : sHostActMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String solveActClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String activity = sHostActMap.get(className);
            DLog.i("className: " + className + " activity: " + activity);
            if (!TextUtils.isEmpty(activity)) {
                sHostActMap.put(className, null); //清除坑位
                return activity;
            }
        }
        return null;
    }

    public static ActivityInfo getPluginActivityInfo(String activityName) {
        return sPluginActMap.get(activityName);
    }

    public static void obtainHostActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        sPackageName = pInfo.packageName;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Host activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            if (activityInfo.name.startsWith(PLUGIN_START_NAME)) {
                sHostActMap.put(activityInfo.name, null);
            }
        }
    }

    public static void obtainPluginActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Plugin activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            sPluginActMap.put(activityInfo.name, activityInfo);
        }
    }

    public static void clear() {
        sHostActMap.clear();
        sPluginActMap.clear();
        ActPitEvent.sActPitEvent = null;
    }

}
