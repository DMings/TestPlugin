package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import com.dming.simple.utils.DLog;

import java.io.File;
import java.lang.reflect.Method;

@Deprecated
public class ActPitEvent {

    private IActPitEvent mActPitEvent;
    private Resources mResource;

    public void setPitActEvent(IActPitEvent iActPitEvent) {
        this.mActPitEvent = iActPitEvent;
        DLog.i("setPitActEvent>>>" + iActPitEvent);
    }

    public void setResources(Resources resources) {
        mResource = resources;
        DLog.i("setResources>>>" + mResource.toString());
    }

    public boolean startActivity(Intent intent, int requestCode, Bundle options) {
        if (mActPitEvent == null) {
            return false;
        }
        return mActPitEvent.startActivityForResult(intent, requestCode, options);
    }

    public Resources getResources() {
        return mResource;
    }

    private void setResources(Context context, File apk) throws Exception {
        mResource = createResources(context, apk);
        DLog.i("setResources>>>" + mResource.toString());
    }

    private Resources createResources(Context context, File apk) throws Exception {
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(apk);
        Resources resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        DLog.i("resources>>>" + resources.toString());
        return resources;
    }

    private AssetManager createAssetManager(File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        Method addAssetPath = am.getClass().getDeclaredMethod("addAssetPath", String.class);
        addAssetPath.invoke(am, apk.getAbsolutePath());
        DLog.i("AssetManager path:  " + apk.getAbsolutePath());
        return am;
    }

}
