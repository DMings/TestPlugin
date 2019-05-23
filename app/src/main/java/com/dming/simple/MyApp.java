package com.dming.simple;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
