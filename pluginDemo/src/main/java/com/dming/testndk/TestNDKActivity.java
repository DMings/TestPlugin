package com.dming.testndk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.dming.simple.PluginActivity;
import com.dming.testndk.one.OneActivity;

public class TestNDKActivity extends PluginActivity {

    static {
        System.loadLibrary("testjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this,"我是在插件弹出来的权限请求",Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},666);
        }

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestNDKActivity.this, OneActivity.class));
            }
        });

        findViewById(R.id.test_jni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ndkMsg = getMsgFromNDK();
                Toast.makeText(TestNDKActivity.this,ndkMsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public native String getMsgFromNDK();

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    @Override
    public void setTheme(int resid) {
//        int[] ints = com.dming.testndk.R.styleable.AppCompatTheme;
//        for (int i = 0;i <ints.length;i++ ){
//            DLog.i("ints=>: "+Integer.toHexString(ints[i]));
//        }
//        DLog.i("AppCompatTheme_windowActionBar=>: "+com.dming.testndk.R.styleable.AppCompatTheme_windowActionBar);
        super.setTheme(resid);
    }
}
