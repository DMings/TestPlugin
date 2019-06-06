package com.dming.testndk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.dming.simple.plugin.activity.PluginActivity;

public class TestActivity extends PluginActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DMUI", "TestActivity bindService");
                startService(new Intent(TestActivity.this, TestService.class));
                bindService(new Intent(TestActivity.this, TestService.class),serviceConnection , Context.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DMUI", "TestActivity unbindService");
                unbindService(serviceConnection);
                stopService(new Intent(TestActivity.this, TestService.class));
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("DMUI", "TestActivity onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("DMUI", "TestActivity onServiceDisconnected");
        }
    };

}
