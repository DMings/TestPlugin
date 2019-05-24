package com.dming.simple;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.plugin.activity.ActPlugin;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActPlugin.startActivity(MainActivity.this, "com.dming.testndk.NDKActivity");
            }
        });
    }


}

