package com.dming.simple.plugin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.util.HashMap;

public class ActPlugin {

    private static HashMap<String, ActivityInfo> sPluginActMap = new HashMap<>();

    public static void startActivity(Activity activity, Intent intent, int requestCode, Bundle options) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = ActPitEvent.getInstance().startActivityForResult(intent, requestCode, options);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startActivity pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                activity.startActivityForResult(intent, requestCode, options);
            } else {
                SToast.showPluginError(activity);
            }
        } else {
            SToast.showPluginError(activity);
        }
    }


    public static ActivityInfo getPluginActivityInfo(String activityName) {
        return sPluginActMap.get(activityName);
    }

    public static void obtainPluginActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Plugin activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            sPluginActMap.put(activityInfo.name, activityInfo);
        }
    }

    public static void clear() {
        sPluginActMap.clear();
        ActPitEvent.sActPitEvent = null;
    }

}
