package com.dming.testplugin.utils;

import android.util.Log;


/**
 * 简单日志类，方便
 * Created by DMing on 2017/9/19.
 */

public class DLog {

    private static String TAG = "DMUI";

    public static boolean DEBUG = true;

    public static void d(String tag, String msg){
        if (DEBUG) Log.d(tag,msg);
    }

    public static void i(String tag, String msg){
        if (DEBUG) Log.i(tag,msg);
    }

    public static void e(String tag, String msg){
        if (DEBUG) Log.e(tag,msg);
    }

    @SuppressWarnings("unused")
    public static void d(String msg) {
        d(TAG,msg);
    }

    @SuppressWarnings("unused")
    public static void i(String msg) {
        i(TAG,msg);
    }

    @SuppressWarnings("unused")
    public static void e(String msg) {
        e(TAG,msg);
    }
}