package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;
import com.dming.simple.utils.Reflector;

import java.io.File;

public class PitResources {

    public static Resources sResource;

    public static void setResources(Context context, File apk) throws Exception {
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(apk);
        sResource = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        DLog.i("setResources>>>"+sResource.toString());
    }

    private static AssetManager createAssetManager(File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        ReflectUtils.invokeMethod(am,"addAssetPath",new Class[]{String.class},apk.getAbsolutePath());
        return am;
    }

}
