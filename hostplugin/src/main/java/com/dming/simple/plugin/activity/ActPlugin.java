package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.simple.utils.DLog;

import java.util.HashMap;
import java.util.Map;

public class ActPlugin {

    private static HashMap<String, String> sActivityMap = new HashMap<>();
    private static String PLUGIN_START_NAME = "com.simple.plugin.Activity";
    private static String sPackageName;

    public static final IPitActEvent sPitActEvent = new IPitActEvent() {
        @Override
        public boolean startActivityForResult(Context context, Intent intent, int requestCode, Bundle options) {
            DLog.i("startActivityForResult --->");
            if (intent.getComponent() != null && sPackageName != null) {
                String component = intent.getComponent().getClassName();
                String key = getActivityStr();
                DLog.i("startActivityForResult key: " + key + " component: " + component);
                if (key == null) {
                    return false;
                }
                sActivityMap.put(key, component);
                intent.setClassName(sPackageName, key);
                return true;
            }
            return false;
        }
    };

    public static String getActivityStr() {
        for (Map.Entry<String, String> entry : sActivityMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String solveActClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String activity = sActivityMap.get(className);
            DLog.i("className: " + className + " activity: " + activity);
            if (!TextUtils.isEmpty(activity)) {
                sActivityMap.put(className, null); //清除坑位
                return activity;
            }
        }
        return null;
    }

    public static void androidManifestActivityPit(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        sPackageName = pInfo.packageName;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Host activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            if (activityInfo.name.startsWith(PLUGIN_START_NAME)) {
                sActivityMap.put(activityInfo.name, null);
            }
        }
    }

}
