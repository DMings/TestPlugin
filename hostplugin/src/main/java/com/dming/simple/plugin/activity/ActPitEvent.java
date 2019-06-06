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
        if (intent.getComponent() != null && ActPlugin.sPackageName != null) {
            String activityName = intent.getComponent().getClassName();
            String activityPit = ActPlugin.findActivityPit(activityName);
            DLog.i("startActivityForResult activityPit: " + activityPit + " activityName: " + activityName);
            if (activityPit == null) {
                return false;
            }
            ActPlugin.sHostActMap.put(activityPit, activityName);
            intent.setClassName(ActPlugin.sPackageName, activityPit);
            ActivityInfo activityInfo = ActPlugin.getPluginActivityInfo(activityName);
            intent.putExtra("ActivityInfo", activityInfo);
            return true;
        }
        return false;
    }


}
