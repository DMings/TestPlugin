package com.dming.testndk;

import android.app.Application;
import com.dming.simple.utils.DLog;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i("TestApplication----->");
    }
}
