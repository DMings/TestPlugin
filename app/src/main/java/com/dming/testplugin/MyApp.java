package com.dming.testplugin;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.dming.testplugin.utils.PatchClassLoaderUtils;

public class MyApp extends Application {

    private String TAG = "DMUI";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        boolean b = PatchClassLoaderUtils.patch(this);
        if (b) {
            PMF.initInThread(this);
            Log.i(TAG, "Patch ClassLoader success");
        } else {
            Log.i(TAG, "Patch ClassLoader false");
        }
    }
}
