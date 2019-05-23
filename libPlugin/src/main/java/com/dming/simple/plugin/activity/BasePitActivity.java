package com.dming.simple.plugin.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BasePitActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (ActPitEvent.startActivity(this,intent, requestCode, options)) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.startActivityForResult(intent, requestCode, options);
            } else {
                super.startActivityForResult(intent, requestCode);
            }
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = ActPitEvent.getResources();
        if(resources != null){
            return resources;
        }
        return super.getResources();
    }

}
