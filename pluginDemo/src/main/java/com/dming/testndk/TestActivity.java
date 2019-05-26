package com.dming.testndk;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.dming.simple.plugin.activity.PluginActivity;

public class TestActivity extends PluginActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view = new View(this);
//        view.setBackgroundColor(0xFFFFFF00);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(params);
//        setContentView(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TestActivity.this, TwoActivity.class));
//            }
//        });
        setContentView(R.layout.activity_main);
    }

}
