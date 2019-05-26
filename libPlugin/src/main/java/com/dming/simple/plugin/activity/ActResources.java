package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.dming.simple.utils.DLog;

import java.io.File;
import java.lang.reflect.Method;

public class ActResources {

    public Resources createResources(Context context, File apk) throws Exception {
        Resources hostResources = context.getResources();
        DLog.i("hostResources>>>"+hostResources.getClass().getClassLoader());
        AssetManager assetManager = createAssetManager(apk);
        Resources resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        DLog.i("resources>>>"+resources.toString());
        return resources;
    }

    private AssetManager createAssetManager(File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        Method addAssetPath = am.getClass().getDeclaredMethod("addAssetPath",String.class );
        addAssetPath.invoke(am,apk.getAbsolutePath());
        DLog.i("AssetManager path:  "+apk.getAbsolutePath());
        return am;
    }

}
