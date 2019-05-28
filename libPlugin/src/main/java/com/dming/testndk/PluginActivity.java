package com.dming.testndk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.plugin.activity.ActPitEvent;
import com.dming.simple.utils.DLog;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String plugin = intent.getStringExtra("Plugin");
        ActivityInfo activityInfo = intent.getParcelableExtra("ActivityInfo");
        super.onCreate(savedInstanceState);
        DLog.i("plugin: "+plugin + " activityInfo: "+activityInfo);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (ActPitEvent.startActivity(intent, requestCode, options)) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.startActivityForResult(intent, requestCode, options);
            } else {
                super.startActivityForResult(intent, requestCode);
            }
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return ActPitEvent.sClassLoader;
//        return super.getClassLoader();
    }

    @Override
    public AssetManager getAssets() {
        Resources resources = ActPitEvent.getResources();
        if(resources != null){
            return resources.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return ActPitEvent.sApplicationInfo;
    }

    @Override
    public Resources getResources() {
        Resources resources = ActPitEvent.getResources();
        if(resources != null){
            DLog.i("getResources>"+resources);
            return resources;
        }
        return super.getResources();
    }

}
