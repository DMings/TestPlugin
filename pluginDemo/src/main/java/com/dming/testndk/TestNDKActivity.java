package com.dming.testndk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.dming.simple.PluginManager;
import com.dming.simple.plugin.activity.PluginActivity;
import com.dming.simple.utils.DLog;

public class TestNDKActivity extends PluginActivity {

    static {
        System.loadLibrary("testjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(TestNDKActivity.this, "我是在插件弹出来的权限请求", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
                }
            }
        });

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TestNDKActivity.this, TestServiceActivity.class), 8989);
            }
        });

        findViewById(R.id.test_jni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ndkMsg = getMsgFromNDK();
                Toast.makeText(TestNDKActivity.this, ndkMsg, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.testReceiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.xxx");
                TestNDKActivity.this.sendBroadcast(intent1);
            }
        });
        findViewById(R.id.testProvider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toProviderName = "com.dming.testndk";
                Uri uri = Uri.parse("content://" + toProviderName + "/testPath_xxxxxxxx");
                Cursor cursor = PluginManager.query(TestNDKActivity.this, uri, null, null, null, null);
                try {
                    DLog.i("Cursor = " + cursor);
                    if (cursor != null) {
                        int count = cursor.getCount();
                        Log.i("DMUI", "cursor count: " + count);
                        Toast.makeText(TestNDKActivity.this.getApplicationContext(), "MainActivity cursor count: " + count, Toast.LENGTH_LONG).show();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }

    public native String getMsgFromNDK();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("act", "TestNDKActivity");
        setResult(2323, intent);
        Toast.makeText(this,"setResult: "+2323,Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
