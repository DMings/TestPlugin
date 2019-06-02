package com.dming.simple;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import com.dming.simple.utils.DLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginManager {

    public static Resources sResources;
    public static ApplicationInfo sApplicationInfo;
    public static ClassLoader sClassLoader;
    private static Object sActPitEvent;
    private static Method sStartActivityForResult;

    public static void setActPicEvent(Object obj) throws NoSuchMethodException {
        sActPitEvent = obj;
        sStartActivityForResult = PluginManager.sActPitEvent.getClass().getDeclaredMethod("startActivityForResult", Intent.class, int.class, Bundle.class);
        DLog.i("PluginManager.sActPitEvent: " + PluginManager.sActPitEvent.getClass().getClassLoader());
    }

    public static boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
        boolean b = false;
        try {
            DLog.i("PluginManager.intent: " + intent.getClass().getClassLoader());
            DLog.i("PluginManager.sStartActivityForResult: " + sStartActivityForResult.getClass().getClassLoader());
            b = (boolean) sStartActivityForResult.invoke(PluginManager.sActPitEvent, intent, requestCode, options);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return b;
    }

}
