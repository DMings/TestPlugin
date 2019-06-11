package com.dming.simple;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
        super();
        int pid = Process.myPid();
        Log.i("DMUI","MyReceiver>>>>> onReceive->"+this.getClass().getSimpleName()+ " pid: "+pid);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int pid = Process.myPid();
        Log.i("DMUI","MyReceiver onReceive->"+this.getClass().getSimpleName()+ " pid: "+pid);
//        Intent i = new Intent(context,MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //添加标记
//        context.startActivity(i);
    }
}
