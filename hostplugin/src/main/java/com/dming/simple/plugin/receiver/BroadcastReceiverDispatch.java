package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.dming.simple.utils.DLog;

import java.util.ArrayList;
import java.util.List;

public class BroadcastReceiverDispatch extends BroadcastReceiver {

    private static List<SubBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    public static void addSubBroadcastReceiver(SubBroadcastReceiver subBroadcastReceiver) {
        mBroadcastReceiverList.add(subBroadcastReceiver);
    }

    public static void clearBroadcastReceiverList() {
        mBroadcastReceiverList.clear();
    }

    public void registerBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.registerReceiver(subReceiver.getReceiver(), subReceiver.getFilter() != null ?
                    subReceiver.getFilter() : new IntentFilter());
        }
    }

    public void unRegisterBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.unregisterReceiver(subReceiver.getReceiver());
        }
    }

    public void dispatchReceiver(Context context, Intent intent) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            if (intent.getStringExtra("pluginReceiver") != null) {
                subReceiver.getReceiver().onReceive(context, intent);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.i("BroadcastReceiverDispatch: " + intent.toString());
        dispatchReceiver(context, intent);
    }
}
