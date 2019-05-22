package com.dming.testplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DMUI","onCreate>>>");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DMUI","onDestroy>>>");
    }
}
