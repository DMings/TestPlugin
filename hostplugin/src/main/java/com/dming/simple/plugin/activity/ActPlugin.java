package com.dming.simple.plugin.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.simple.PMF;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ActPlugin {

    private static HashMap<String, String> sActivityMap = new HashMap<>();
    private static String PLUGIN_START_NAME = "com.simple.plugin.Activity";
    private static String sPackageName;

    public static void init(ClassLoader plugClassLoader,Context context,File apkFile){
        try {
            ReflectUtils.invokeMethod(plugClassLoader, PitResources.class.getName(),
                    "setResources", null, new Class[]{Context.class, File.class}, context, apkFile);
            ReflectUtils.invokeMethod(plugClassLoader, ActPitEvent.class.getName(),
                    "setPitActEvent", null, new Class[]{IActPitEvent.class}, ActPlugin.sPitActEvent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static final IActPitEvent sPitActEvent = new IActPitEvent() {
        @Override
        public boolean startActivityForResult(Context context, Intent intent, int requestCode, Bundle options) {
            if (intent.getComponent() != null && sPackageName != null) {
                String component = intent.getComponent().getClassName();
                String key = getActivityStr();
                DLog.i("startActivityForResult key: " + key + " component: " + component);
                if (key == null) {
                    return false;
                }
                sActivityMap.put(key, component);
                intent.setClassName(sPackageName, key);
                return true;
            }
            return false;
        }
    };

    public static void startActivity(Context context,String activityCls){
        startActivity(context,activityCls,-1);
    }

    public static void startActivity(Context context,String activityCls, int requestCode) {
        startActivity(context,activityCls,requestCode,null);
    }

    public static void startActivity(Context context,String activityCls, int requestCode, Bundle options){
        PMF.checkInit();
        Intent intent = new Intent();
        ComponentName cn = new ComponentName(context.getPackageName(), activityCls);
        intent.setComponent(cn);
        ActPlugin.sPitActEvent.startActivityForResult(context,intent,requestCode,options);
        DLog.i("pkgName>"+cn.getPackageName()+" >>> name-> "+cn.getClassName());
        context.startActivity(intent);
    }

    public static String getActivityStr() {
        for (Map.Entry<String, String> entry : sActivityMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String solveActClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String activity = sActivityMap.get(className);
            DLog.i("className: " + className + " activity: " + activity);
            if (!TextUtils.isEmpty(activity)) {
                sActivityMap.put(className, null); //清除坑位
                return activity;
            }
        }
        return null;
    }

    public static void androidManifestActivityPit(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        sPackageName = pInfo.packageName;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Host activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            if (activityInfo.name.startsWith(PLUGIN_START_NAME)) {
                sActivityMap.put(activityInfo.name, null);
            }
        }
    }

}
