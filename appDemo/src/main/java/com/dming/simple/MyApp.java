package com.dming.simple;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static boolean b = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if(!b) {
            b = true;
            SPlugin.init(this);
        }
    }

}
