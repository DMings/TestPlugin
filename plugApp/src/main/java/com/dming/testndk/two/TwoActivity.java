package com.dming.testndk.two;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.dming.testplugin.BaseDexActivity;

import androidx.annotation.Nullable;

public class TwoActivity extends BaseDexActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(0xFF0000FF);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        setContentView(view);
    }

}
