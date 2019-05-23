package com.dming.testplugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.dming.testndk.utils.Reflector;

import java.io.File;

public class DexResources {

    public static Resources sResource;

    public static void initResources(Context context, File apk) throws Exception {
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(apk);
        sResource = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        Log.i("DMUI","initResources>>>"+sResource.toString());
    }

    private static AssetManager createAssetManager(File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        Reflector.with(am).method("addAssetPath", String.class).call(apk.getAbsolutePath());
        return am;
    }

}
