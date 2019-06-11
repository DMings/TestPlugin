package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;

import java.util.ArrayList;
import java.util.List;

public class RecPlugin {

    public static final String RECEIVER_NAME = BroadcastReceiverDispatch.class.getName();
    public static String sPackageName;
    public final static String PLUGIN_RECEIVER_FLAG = "pluginReceiver";
    private static List<SubBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    public static void addSubBroadcastReceiver(SubBroadcastReceiver subBroadcastReceiver) {
        mBroadcastReceiverList.add(subBroadcastReceiver);
    }

    public static void clearBroadcastReceiverList(Context context) {
        unRegisterBroadcastReceiver(context);
        mBroadcastReceiverList.clear();
    }

    public static void registerBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.registerReceiver(subReceiver.getReceiver(), subReceiver.getFilter() != null ?
                    subReceiver.getFilter() : new IntentFilter());
            DLog.i("subReceiver>"+subReceiver.getReceiver().getClass().getSimpleName());
        }
    }

    public static void unRegisterBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.unregisterReceiver(subReceiver.getReceiver());
        }
    }

    public static void dealPluginReceiver(Context context, PackageInfo pInfo) {
        ActivityInfo[] receivers = pInfo.receivers;
        sPackageName = pInfo.packageName;
        clearBroadcastReceiverList(context);
        for (ActivityInfo receiverInfo : receivers) {
            DLog.i("SPlugin ActivityInfo receivers>" + receiverInfo.name + " " + receiverInfo.flags);
            try {
                Class receiver = SPlugin.getInstance().getClassLoader().loadClass(receiverInfo.name);
                addSubBroadcastReceiver(new SubBroadcastReceiver((BroadcastReceiver) receiver.newInstance()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        registerBroadcastReceiver(context);
    }

    public static void dispatchReceiver(Context context, Intent intent) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            DLog.i("subReceiver.getReceiver Name " + subReceiver.getReceiver().getClass().getSimpleName());
            String recName = intent.getStringExtra(PLUGIN_RECEIVER_FLAG);
            if (recName != null &&
                    subReceiver.getReceiver().getClass().getSimpleName().equals(recName)) {
                subReceiver.getReceiver().onReceive(context, intent);
            }
        }
    }

}
