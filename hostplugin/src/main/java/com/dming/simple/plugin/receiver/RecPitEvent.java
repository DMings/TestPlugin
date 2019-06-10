package com.dming.simple.plugin.receiver;

import android.content.Intent;
import com.dming.simple.utils.DLog;

public class RecPitEvent {

    private static volatile RecPitEvent sRecPitEvent;

    public static RecPitEvent getInstance() {
        if (sRecPitEvent == null) {
            synchronized (RecPitEvent.class) {
                if (sRecPitEvent == null) {
                    sRecPitEvent = new RecPitEvent();
                }
            }
        }
        return sRecPitEvent;
    }

    public boolean sendBroadcast(Intent intent){
        if (intent.getComponent() != null && RecPlugin.sPackageName != null) {
            String recName = intent.getComponent().getClassName();
            DLog.i("sendBroadcast recName: " + recName);
            intent.setClassName(RecPlugin.sPackageName, RecPlugin.RECEIVER_NAME);
            intent.putExtra(RecPlugin.PLUGIN_RECEIVER_FLAG,recName);
            return true;
        }
        return false;
    }

}
