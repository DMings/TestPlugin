package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dming.simple.OnPluginInitListener;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;

public class ReceiverDispatch extends BroadcastReceiver {

    public ReceiverDispatch() {
        DLog.i("ReceiverDispatch onCreate");
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DLog.i("ReceiverDispatch: " + intent.toString());
        RecPlugin.dispatchReceiver(context, intent);
    }
}
