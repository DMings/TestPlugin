package com.dming.simple;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.utils.DLog;

public class TestHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enteralpha, R.anim.exitalpha);
        setContentView(R.layout.layout_test);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED ||
//                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)) {
//            Toast.makeText(TestHostActivity.this,"弹出权限请求,动画异常",Toast.LENGTH_SHORT).show();
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},666);
//        }
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TestHostActivity.this, TestHostActivity.this.getString(R.string.test_toast),Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(TestHostActivity.this, TestHostActivity2.class));
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    DLog.i("uri: " + uri.toString());
                    String path = UriUtils.getFilePathFromUri(this, uri);
                    DLog.i("path: " + path);
                }
            }
        }
    }


    @Override
    public void onEnterAnimationComplete() {
        DLog.i("onEnterAnimationComplete>>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(950);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                DLog.i("postDelayed--------->");
//                new Handler().postDelayed(this,1000);
//            }
//        },200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DLog.i("onPause----/---->");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        DLog.i("onRestoreInstanceState------->");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Bitmap bitmap = Bitmap.createBitmap(5000,5000, Bitmap.Config.ARGB_8888);
//        outState.putParcelable("bit",bitmap);
        DLog.i("onSaveInstanceState----5/--->");
    }

    @Override
    protected void onStop() {
        super.onStop();
        DLog.i("onStop------->");
    }
}
