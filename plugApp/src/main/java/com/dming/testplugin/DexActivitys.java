package com.dming.testplugin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DexActivitys {

    public static IDealDexActivity iDealDexActivity;

    public static void setDealDexActivity(IDealDexActivity iDealDexActivity) {
        DexActivitys.iDealDexActivity = iDealDexActivity;
        Log.i("DMUI","IDealDexActivity>>>"+iDealDexActivity);
    }

    public static boolean dealStartActivity(Intent intent, int requestCode, Bundle options){
        if(iDealDexActivity == null){
            return false;
        }
        return iDealDexActivity.startActivityForResult(intent, requestCode, options);
    }

}
