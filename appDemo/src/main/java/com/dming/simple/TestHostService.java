package com.dming.simple;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import com.dming.simple.utils.DLog;

public class TestHostService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i("onCreate: " );
    }

    @Override
    public IBinder onBind(Intent intent) {
        DLog.i("onBind: " + intent.toString());
        Handler handler = new Handler();
        Messenger messenger = new Messenger(handler);
        return messenger.getBinder();
    }

}
