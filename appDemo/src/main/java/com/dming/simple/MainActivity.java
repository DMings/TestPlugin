package com.dming.simple;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.plugin.provider.ProPitEvent;
import com.dming.simple.plugin.provider.ProviderDispatch;
import com.dming.simple.plugin.service.ServicePlugin;
import com.dming.simple.utils.DLog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SPlugin.initPlugin(this, "NDK_1.0.8.apk", new OnPluginInitListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "插件加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "插件加载失败", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_plugin_act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestNDKActivity");
                intent.setComponent(cn);
                ActPlugin.startActivity(MainActivity.this, intent, -1, null);
            }
        });

        findViewById(R.id.btn_plugin_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestService");
                intent.setComponent(cn);
                ServicePlugin.startService(MainActivity.this, intent);
            }
        });

        findViewById(R.id.btn_plugin_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestService");
                intent.setComponent(cn);
                ServicePlugin.stopService(MainActivity.this, intent);
            }
        });

        findViewById(R.id.btn_plugin_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestService");
                intent.setComponent(cn);
                ServicePlugin.bindService(MainActivity.this, intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_plugin_unbind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicePlugin.unbindService(MainActivity.this, mConnection);
            }
        });
        findViewById(R.id.btn_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.xxx");
                MainActivity.this.sendBroadcast(intent);
            }
        });
        findViewById(R.id.btn_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 其他进程或者本进程
                final String toProviderName = "com.dming.testndk";
                String path = "testPath";
                Uri uri = Uri.parse("content://" + ProviderDispatch.AUTHORITIES + "/" + toProviderName + "/" + path);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    DLog.i("Cursor = " + cursor);
                    if (cursor != null) {
                        int count = cursor.getCount();
                        Log.i("DMUI","cursor count: " + count);
                        Toast.makeText(MainActivity.this.getApplicationContext(), "MainActivity cursor count: " + count, Toast.LENGTH_LONG).show();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                //  仅本进程
                Uri uri2 = Uri.parse("content://" + toProviderName + "/testPath2");
                cursor = ProPitEvent.getInstance().query(uri2, null, null, null, null);
                try {
                    DLog.e("Cursor2 = " + cursor);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Messenger mService = new Messenger(service);
            try {
                Message message = new Message();
                message.what = 911;
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            DLog.i("onServiceConnected: " + className);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            DLog.i("onServiceDisconnected: " + componentName.getClassName());
            ServicePlugin.clearServicePit(componentName.getClassName());
        }

    };
}

