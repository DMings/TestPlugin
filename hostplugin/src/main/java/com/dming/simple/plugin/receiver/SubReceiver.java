package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class SubReceiver {

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private int flags;

    public SubReceiver(BroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    public SubReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        this.receiver = receiver;
        this.filter = filter;
    }

    public SubReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
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
