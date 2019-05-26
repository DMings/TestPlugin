package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import com.dming.simple.utils.DLog;

import java.io.File;
import java.lang.reflect.Method;

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
        sResource = createResources(context,apk);
        DLog.i("setResources>>>"+sResource.toString());
    }

    private static Resources createResources(Context context, File apk) throws Exception {
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(apk);
        Resources resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        DLog.i("resources>>>"+resources.toString());
        return resources;
    }

    private static AssetManager createAssetManager(File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        Method addAssetPath = am.getClass().getDeclaredMethod("addAssetPath",String.class );
        addAssetPath.invoke(am,apk.getAbsolutePath());
        DLog.i("AssetManager path:  "+apk.getAbsolutePath());
        return am;
    }

}
