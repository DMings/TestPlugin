package com.dming.simple;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.plugin.service.ServicePlugin;


public class MainActivity extends AppCompatActivity {

    private static boolean b = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!b){
            b = true;
            SPlugin.initPlugin(this, "NDK_1.0.4.apk", new OnPluginInitListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "插件加载成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, "插件加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        findViewById(R.id.btn_host).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestHostActivity.class));
            }
        });

        findViewById(R.id.btn_plugin_act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestNDKActivity");
                intent.setComponent(cn);
                ActPlugin.getInstance().startActivity(MainActivity.this, intent,-1,null);
            }
        });

        findViewById(R.id.btn_plugin_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestService");
                intent.setComponent(cn);
                ServicePlugin.getInstance().startService(MainActivity.this, intent);
            }
        });

        findViewById(R.id.btn_plugin_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestService");
                intent.setComponent(cn);
                ServicePlugin.getInstance().stopService(MainActivity.this, intent);
//                bindService()
//                unbindService();
            }
        });

    }


}

