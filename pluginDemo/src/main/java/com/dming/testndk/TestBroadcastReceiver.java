package com.dming.testndk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dming.simple.utils.DLog;

public class TestBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.i("TestBroadcastReceiver onReceive: "+intent.toString());
        Toast.makeText(context.getApplicationContext(),"广播: "+intent.toString(),Toast.LENGTH_LONG).show();
    }
}
