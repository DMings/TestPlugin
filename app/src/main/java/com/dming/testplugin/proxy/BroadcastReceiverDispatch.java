package com.dming.testplugin.proxy;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BroadcastReceiverDispatch {

    private List<SubBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    public void addSubBroadcastReceiver(SubBroadcastReceiver subBroadcastReceiver){
        mBroadcastReceiverList.add(subBroadcastReceiver);
    }

    public void clearBroadcastReceiverList(){
        mBroadcastReceiverList.clear();
    }

    public void registerBroadcastReceiver(Context context){
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.registerReceiver(subReceiver.getReceiver(),subReceiver.getFilter());
        }
    }

    public void unRegisterBroadcastReceiver(Context context){
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.unregisterReceiver(subReceiver.getReceiver());
        }
    }

}
