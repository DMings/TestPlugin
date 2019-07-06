package com.dming.simple.plugin.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.dming.simple.SPlugin;
import com.dming.simple.plugin.service.PluginProxyService;
import com.dming.simple.utils.DLog;

public class ActPitEvent {

    static volatile ActPitEvent sActPitEvent;

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
        if (intent.getComponent() != null) {
            String activityName = intent.getComponent().getClassName();
            ActivityInfo activityInfo = ActPlugin.getPluginActivityInfo(activityName);
            intent.putExtra("ActivityInfo", activityInfo);
            intent.putExtra("ClassName",activityName);
            intent.setClassName(SPlugin.getInstance().getHostPkgName(), PluginProxyActivity.class.getName());
            DLog.i("startActivityForResult activityName: " + activityName);
            return true;
        }
        return false;
    }


}
