package com.dming.testndk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

@SuppressLint("Registered")
public class TestService extends Service {

    public final static int MSG_SAY_HELLO = 911;
    private Messenger mMessenger = new Messenger(new IncomingHandler(this));

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i( "TestService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        DLog.i( "TestService onBind");
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        DLog.i( "TestService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DLog.i( "TestService onDestroy");
    }

    static class IncomingHandler extends Handler {

        private Context mContext;

        IncomingHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            DLog.i("msg.what: " + msg.what);
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(this.mContext.getApplicationContext(), "Hello 911!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}

