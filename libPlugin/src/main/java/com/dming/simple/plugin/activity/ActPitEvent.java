package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public class ActPitEvent {

    public static IPitActEvent iPitActEvent;

    public static void setPitActEvent(IPitActEvent iPitActEvent) {
        ActPitEvent.iPitActEvent = iPitActEvent;
        Log.i("DMUI","IPitActEvent>>>"+iPitActEvent);
    }

    public static boolean startActivity(Context context,Intent intent, int requestCode, Bundle options){
        if(iPitActEvent == null){
            return false;
        }
        return iPitActEvent.startActivityForResult(context,intent, requestCode, options);
    }

    public static Resources getResources() {
        if (PitResources.sResource != null) {
            return PitResources.sResource;
        }
        return null;
    }

}
