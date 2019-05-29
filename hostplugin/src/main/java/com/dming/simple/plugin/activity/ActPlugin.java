package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActPlugin {

    private static HashMap<String, String> sHostActMap = new HashMap<>();
    private static HashMap<String, ActivityInfo> sPluginActMap = new HashMap<>();
    private static String PLUGIN_START_NAME = "com.dming.simple.Activity";
    private static String sPackageName;

    public static void initActivityPlugin(Context context, File apkFile) throws Exception {
//        ActPitEvent.setResources(context, apkFile);
////        createResources(context, apkFile);
//        ActPitEvent.setPitActEvent(ActPlugin.sPitActEvent);
    }

//    private static void createResources(Context context, File apk) throws Exception {
//        SPlugin sPlugin = SPlugin.getInstance();
//        Resources hostResources = context.getResources();
//        Class<?> clazz = sPlugin.mPlugClassLoader.loadClass(Resources.class.getName());
//        AssetManager assetManager = createAssetManager(apk);
//        Constructor<?> constructor = clazz.getConstructor(AssetManager.class, DisplayMetrics.class, Configuration.class);
//        Resources resources = (Resources) constructor.newInstance(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
//        ActPitEvent.sResource = resources;
////        Resources resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
//        DLog.i("resources>>>"+resources.toString());
//    }
//
//    private static AssetManager createAssetManager(File apk) throws Exception {
//        AssetManager am = AssetManager.class.newInstance();
//        Method addAssetPath = am.getClass().getDeclaredMethod("addAssetPath",String.class );
//        addAssetPath.invoke(am,apk.getAbsolutePath());
//        DLog.i("AssetManager path:  "+apk.getAbsolutePath());
//        return am;
//    }

//    public static void init(ClassLoader plugClassLoader,Context context,File apkFile){
//        try {
//            ReflectUtils.invokeMethod(plugClassLoader, PitResources.class.getName(),
//                    "setResources", null, new Class[]{Context.class, File.class}, context, apkFile);
//            ReflectUtils.invokeMethod(plugClassLoader, ActPitEvent.class.getName(),
//                    "setPitActEvent", null, new Class[]{IActPitEvent.class}, ActPlugin.sPitActEvent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    public final static IActPitEvent sPitActEvent = new IActPitEvent() {
        @Override
        public boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
            if (intent.getComponent() != null && sPackageName != null) {
                String activityName = intent.getComponent().getClassName();
                String activityPit = getActivityStr();
                DLog.i("startActivityForResult activityPit: " + activityPit + " activityName: " + activityName);
                if (activityPit == null) {
                    return false;
                }
                sHostActMap.put(activityPit, activityName);
                intent.setClassName(sPackageName, activityPit);
                ActivityInfo activityInfo = getPluginActivityInfo(activityName);
                intent.putExtra("ActivityInfo", activityInfo);
                return true;
            }
            return false;
        }
    };

//    public static void startActivity(Context context, String activityCls) {
//        startActivity(context, activityCls, -1);
//    }
//
//    public static void startActivity(Context context, String activityCls, int requestCode) {
//        startActivity(context, activityCls, requestCode, null);
//    }

    public static void startActivity(Context context, Intent intent, int requestCode, Bundle options) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = sPitActEvent.startActivityForResult(intent, requestCode, options);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startActivity pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                context.startActivity(intent);
            } else {
                SToast.showPluginError(context);
            }
        } else {
            SToast.showPluginError(context);
        }
    }

    public static String getActivityStr() {
        for (Map.Entry<String, String> entry : sHostActMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String solveActClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String activity = sHostActMap.get(className);
            DLog.i("className: " + className + " activity: " + activity);
            if (!TextUtils.isEmpty(activity)) {
                sHostActMap.put(className, null); //清除坑位
                return activity;
            }
        }
        return null;
    }

    public static ActivityInfo getPluginActivityInfo(String activityName) {
        return sPluginActMap.get(activityName);
    }

    public static void obtainHostActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        sPackageName = pInfo.packageName;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Host activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            if (activityInfo.name.startsWith(PLUGIN_START_NAME)) {
                sHostActMap.put(activityInfo.name, null);
            }
        }
    }

    public static void obtainPluginActivity(PackageInfo pInfo) {
        ActivityInfo[] activities = pInfo.activities;
        for (ActivityInfo activityInfo : activities) {
            DLog.i("Plugin activityInfo>" + activityInfo.name + " packageName: " + activityInfo.packageName);
            sPluginActMap.put(activityInfo.name, activityInfo);
        }
    }

}
