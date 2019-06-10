package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.dming.simple.utils.DLog;

import java.util.ArrayList;
import java.util.List;

public class BroadcastReceiverDispatch extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.i("BroadcastReceiverDispatch: " + intent.toString());
        RecPlugin.dispatchReceiver(context, intent);
    }
}
