package com.dming.simple;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.dming.simple.plugin.activity.ActPitEvent;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.utils.DLog;

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SPlugin.init(this);
    }

    @Override
    public Resources getResources() {
        DLog.e("getResources--------p------------>>>");
        return ActPitEvent.getResources();
//        return super.getResources();
    }
}
