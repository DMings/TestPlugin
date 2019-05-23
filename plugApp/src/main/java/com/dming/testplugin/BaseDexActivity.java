package com.dming.testplugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BaseDexActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (DexActivitys.dealStartActivity(intent, requestCode, options)) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.startActivityForResult(intent, requestCode, options);
            } else {
                super.startActivityForResult(intent, requestCode);
            }
            return;
        }
    }


    @Override
    public Resources getResources() {
        Log.i("DMUI","DexResources.sResource: "+DexResources.sResource);
        if (DexResources.sResource != null) {
            return DexResources.sResource;
        }
        return super.getResources();
    }

}
