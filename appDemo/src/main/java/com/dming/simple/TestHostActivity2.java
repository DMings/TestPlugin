package com.dming.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.utils.DLog;

public class TestHostActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestHostActivity2.this, TestHostActivity2.this.getString(R.string.test_toast), Toast.LENGTH_SHORT).show();
//                getContentResolver().regi
//                Looper.getMainLooper().quit();
            }
        });
    }

    @Override
    public void onEnterAnimationComplete() {
        DLog.i("onEnterAnimationComplete-2222222222222222>>>>>>>>>>>>>>>>>>>>>>");
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
}
