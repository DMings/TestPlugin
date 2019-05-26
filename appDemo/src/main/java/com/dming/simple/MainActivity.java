package com.dming.simple;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.plugin.activity.ActPlugin;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SPlugin.initPlugin(this, "NDK_1.0.3.apk", new OnPluginInitListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "插件加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "插件加载失败", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(MainActivity.this.getPackageName(), "com.dming.testndk.TestActivity");
                intent.setComponent(cn);
                intent.putExtra("Plugin","xxxxxxxx");
//                intent.putExtra("","");
                ActPlugin.startActivity(MainActivity.this, intent,-1,null);
            }
        });
    }


}

