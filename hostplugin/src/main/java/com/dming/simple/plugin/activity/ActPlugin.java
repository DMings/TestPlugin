package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActPlugin {

    public HashMap<String, String> sHostActMap = new HashMap<>();
    private HashMap<String, ActivityInfo> sPluginActMap = new HashMap<>();
    private String PLUGIN_START_NAME = "com.dming.simple.Activity";
    public String sPackageName;

    private static volatile ActPlugin sActPlugin;

    public static ActPlugin getInstance() {
        if (sActPlugin == null) {
            synchronized (ActPlugin.class) {
                if (sActPlugin == null) {
                    sActPlugin = new ActPlugin();
                }
            }
        }
        return sActPlugin;
    }


    public void startActivity(Context context, Intent intent, int requestCode, Bundle options) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = ActPitEvent.getInstance().startActivityForResult(intent, requestCode, options);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startActivity pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                context.startActivity(intent);
            } else {
                SToast.showPluginError(context);
            }
        } else {
            SToast.showPluginError(context);
        }
    }

    public String getActivityStr() {
        for (Map.Entry<String, String> entry : sHostActMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String solveActClass(String className) {
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

    public ActivityInfo getPluginActivityInfo(String activityName) {
        return sPluginActMap.get(activityName);
    }

    public void obtainHostActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        sPackageName = pInfo.packageName;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Host activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            if (activityInfo.name.startsWith(PLUGIN_START_NAME)) {
                sHostActMap.put(activityInfo.name, null);
            }
        }
    }

    public void obtainPluginActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Plugin activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            sPluginActMap.put(activityInfo.name, activityInfo);
        }
    }

}
