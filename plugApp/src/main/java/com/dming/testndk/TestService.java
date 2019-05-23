package com.dming.testndk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

@SuppressLint("Registered")
public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DMUI","TestService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DMUI","TestService onDestroy");
    }
}
