package com.dming.testndk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.dming.simple.plugin.activity.PluginActivity;
import com.dming.simple.utils.DLog;

public class TestServiceActivity extends PluginActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i( "TestServiceActivity startService");
                startService(new Intent(TestServiceActivity.this, TestService.class));
            }
        });

        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i( "TestServiceActivity bindService");
                bindService(new Intent(TestServiceActivity.this, TestService.class),serviceConnection , Context.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i( "TestServiceActivity unbindService");
                unbindService(serviceConnection);
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i( "TestServiceActivity stopService");
                stopService(new Intent(TestServiceActivity.this, TestService.class));
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DLog.i( "TestServiceActivity onServiceConnected");
            Toast.makeText(TestServiceActivity.this,"onServiceConnected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            DLog.i( "TestServiceActivity onServiceDisconnected");
        }
    };

}
