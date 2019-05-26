package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.dming.simple.utils.DLog;

import java.io.File;

public class ActPitEvent {

    public static IActPitEvent sActPitEvent;
    public static Resources sResource;

    public static void setPitActEvent(IActPitEvent iActPitEvent) {
        ActPitEvent.sActPitEvent = iActPitEvent;
        DLog.i("setPitActEvent>>>"+ iActPitEvent);
    }

    public static boolean startActivity(Intent intent, int requestCode, Bundle options){
        if(sActPitEvent == null){
            return false;
        }
        return sActPitEvent.startActivityForResult(intent, requestCode, options);
    }

    public static Resources getResources() {
        if (sResource != null) {
            return sResource;
        }
        return null;
    }

    public static void setResources(Context context, File apk) throws Exception {
        ActResources actResources = new ActResources();
        sResource = actResources.createResources(context,apk);
        DLog.i("setResources>>>"+sResource.toString());
    }



}
