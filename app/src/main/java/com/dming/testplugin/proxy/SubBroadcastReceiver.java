package com.dming.testplugin.proxy;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class SubBroadcastReceiver {

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private int flags;

    public SubBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
        this.receiver = receiver;
        this.filter = filter;
        this.flags = flags;
    }

    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    public IntentFilter getFilter() {
        return filter;
    }

    public int getFlags() {
        return flags;
    }
}
