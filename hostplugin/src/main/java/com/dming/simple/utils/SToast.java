package com.dming.simple.utils;

import android.content.Context;
import android.widget.Toast;

public class SToast {

    public static void showPluginError(Context context){
        Toast.makeText(context,"插件失败",Toast.LENGTH_SHORT).show();
    }

}
