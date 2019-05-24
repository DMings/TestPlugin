package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import com.dming.simple.utils.DLog;

public class ActPitEvent {

    public static IActPitEvent sActPitEvent;

    public static void setPitActEvent(IActPitEvent iActPitEvent) {
        ActPitEvent.sActPitEvent = iActPitEvent;
        DLog.i("setPitActEvent>>>"+ iActPitEvent);
    }

    public static boolean startActivity(Context context,Intent intent, int requestCode, Bundle options){
        if(sActPitEvent == null){
            return false;
        }
        return sActPitEvent.startActivityForResult(context,intent, requestCode, options);
    }

    public static Resources getResources() {
        if (PitResources.sResource != null) {
            return PitResources.sResource;
        }
        return null;
    }

}
