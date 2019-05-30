package com.dming.simple.plugin.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.dming.simple.utils.DLog;

public class ActPitEvent {

    private static volatile ActPitEvent sActPitEvent;

    public static ActPitEvent getInstance() {
        if (sActPitEvent == null) {
            synchronized (ActPitEvent.class) {
                if (sActPitEvent == null) {
                    sActPitEvent = new ActPitEvent();
                }
            }
        }
        return sActPitEvent;
    }

    public boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
        ActPlugin actPlugin = ActPlugin.getInstance();
        if (intent.getComponent() != null && actPlugin.sPackageName != null) {
            String activityName = intent.getComponent().getClassName();
            String activityPit = actPlugin.getActivityStr();
            DLog.i("startActivityForResult activityPit: " + activityPit + " activityName: " + activityName);
            if (activityPit == null) {
                return false;
            }
            actPlugin.sHostActMap.put(activityPit, activityName);
            intent.setClassName(actPlugin.sPackageName, activityPit);
            ActivityInfo activityInfo = actPlugin.getPluginActivityInfo(activityName);
            intent.putExtra("ActivityInfo", activityInfo);
            return true;
        }
        return false;
    }


}
