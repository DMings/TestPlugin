package com.dming.simple.plugin.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.PluginManager;
import com.dming.simple.utils.DLog;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context context) {
        PluginContext pluginContext = new PluginContext(context,
                PluginManager.sResources, PluginManager.sApplicationInfo, PluginManager.sClassLoader);
        super.attachBaseContext(pluginContext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        ActivityInfo activityInfo = intent.getParcelableExtra("ActivityInfo");
        setTheme(activityInfo != null && activityInfo.theme != 0 ? activityInfo.theme : getApplicationInfo().theme);
        if(activityInfo != null){
            DLog.i("activityInfo.theme: "+activityInfo.theme);
        }
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (PluginManager.startActivityForResult(intent, requestCode, options)) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.startActivityForResult(intent, requestCode, options);
            } else {
                super.startActivityForResult(intent, requestCode);
            }
        }
    }

}
