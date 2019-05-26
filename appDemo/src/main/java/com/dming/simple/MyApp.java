package com.dming.simple;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SPlugin.init(this);
    }
}
