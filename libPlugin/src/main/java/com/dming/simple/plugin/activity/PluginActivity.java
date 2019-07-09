package com.dming.simple.plugin.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.dming.simple.PluginManager;
import com.dming.simple.utils.DLog;

public class PluginActivity extends Activity {

    @Override
    protected void attachBaseContext(Context context) {
        PluginContext pluginContext = new PluginContext(context);
        super.attachBaseContext(pluginContext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PluginManager.sClassLoader != null) {
            Intent intent = getIntent();
            ActivityInfo activityInfo = intent.getParcelableExtra("ActivityInfo");
            setTheme(activityInfo != null && activityInfo.theme != 0 ? activityInfo.theme : getApplicationInfo().theme);
            if (activityInfo != null) {
                DLog.i("activityInfo.theme: " + activityInfo.theme);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (PluginManager.sClassLoader != null) {
            if (PluginManager.startActivityForResult(intent, requestCode, options)) {
                super.startActivityForResult(intent, requestCode, options);
            }
        } else {
            super.startActivityForResult(intent, requestCode, options);
        }
    }

}
