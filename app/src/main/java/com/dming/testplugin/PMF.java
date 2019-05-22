package com.dming.testplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.dming.testplugin.utils.DLog;
import com.dming.testplugin.utils.ReflectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PMF {

    private static final String TAG = "RePluginClassLoader";

    private static volatile FClassLoader sPlugClassLoader;
    private static volatile File sApkFile;
    private static Context sContext;

    public static void ensureInit(Context context) {
        sContext = context;
        if (sPlugClassLoader == null) {
            synchronized (PMF.class) {
                if (sPlugClassLoader == null) {
                    sPlugClassLoader = generateClassLoader(context);

                    try {
                        ReflectUtils.invokeMethod(sPlugClassLoader, "com.dming.testplugin.DexResources",
                                "initResources", null, new Class[]{Context.class, File.class}, context, sApkFile);
                        ReflectUtils.invokeMethod(sPlugClassLoader, "com.dming.testplugin.DexActivitys",
                                "setDealDexActivity", null, new Class[]{IDealDexActivity.class}, iDealDexActivity);
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
            }
        }
    }

    public static void initInThread(Context context) {
        sContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ensureInit(sContext);
            }
        }).start();
    }

    public static void checkInit() {
        ensureInit(sContext);
    }
//
    public static final Class<?> loadClass(String className, boolean resolve) {
        Class<?> clazz = null;
//        if(className.equals("androidx.appcompat.app.AppCompatActivity")){
//            DLog.i(TAG, "className->" + className);
//            try {
//                return sPlugClassLoader.getParent().loadClass("com.dming.testplugin.HookActivity");
//            } catch (ClassNotFoundException e) {
//                DLog.i("HookActivity: "+e.getException());
//            }
//        }
        if (sPlugClassLoader != null) {
//            DLog.i(TAG, "className>" + className);
            if (className.startsWith(sPluginActivity)) {
                String activity = sActivityMap.get(className);
                DLog.i("className: " + className + " activity: " + activity);
                if (!TextUtils.isEmpty(activity)) {
                    sActivityMap.put(className, null); //清除坑位
                    try {
                        clazz = sPlugClassLoader.loadPluginClass(activity);
                    } catch (ClassNotFoundException e) {
                        DLog.i("PlugClassLoader: "+e.getException());
                    }
                }
            }
        }
        return clazz;
    }

    public static String getActivityStr() {
        for (Map.Entry<String, String> entry : sActivityMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static final IDealDexActivity iDealDexActivity = new IDealDexActivity() {
        @Override
        public boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
            DLog.i("startActivityForResult --->");
            if (intent.getComponent() != null) {
                String component = intent.getComponent().getClassName();
                String key = getActivityStr();
                DLog.i("startActivityForResult key: " + key + " component: " + component);
                if (key == null) {
                    return false;
                }
                sActivityMap.put(key, component);
                intent.setClassName(sContext.getPackageName(), key);
                return true;
            }
//            if(intent.getComponent() != null && intent.getComponent().getClassName().contains("NDKActivity")){
//                intent.setClassName(sContext.getPackageName(),"com.dming.testplugin.PluginActivityS2");
//                return true;
//            }
            return false;
        }
    };

    private static File getDexFile(Context context, String sourceFileName) {
        sApkFile = new File(context.getFilesDir(), "odex_" + sourceFileName);
        try {
            InputStream inputStream = context.getAssets().open(sourceFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(sApkFile);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sApkFile;
    }

    public static FClassLoader generateClassLoader(Context context) {
        FClassLoader classLoader = null;
        try {
            File twoFile = getDexFile(context, "NDK_1.0.1.apk");
            File ndkOutputDir = dealSo();
            File dexOutputDir = context.getDir("dex", Activity.MODE_PRIVATE);
            classLoader = new FClassLoader(twoFile.getAbsolutePath(),// dexPath
                    dexOutputDir.getAbsolutePath(),// optimizedDirectory
                    ndkOutputDir.getAbsolutePath(),
//                    ClassLoader.getSystemClassLoader()
                    context.getClassLoader()
            );
            initHostPkgInfo();
            getPluginInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classLoader;
    }

    private static HashMap<String, String> sActivityMap = new HashMap<>();
    private static String sPluginActivity = "com.dming.testplugin.PluginActivity";

    public static void initHostPkgInfo() {
        long time = System.currentTimeMillis();
        PackageManager packageManager = sContext.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = packageManager.getPackageInfo(sContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DLog.i("host time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ActivityInfo[] activities = pInfo.activities;
            for (ActivityInfo activityInfo : activities) {
                DLog.i("host activityInfo>" + activityInfo.name + " packageName: "+activityInfo.packageName );
                if (activityInfo.name.startsWith(sPluginActivity)) {
                    sActivityMap.put(activityInfo.name, null);
                }
            }
        }
    }

    public static void getPluginInfo() {
        String apkPath = sApkFile.getAbsolutePath();
        long time = System.currentTimeMillis();
        PackageManager packageManager = sContext.getPackageManager();
        PackageInfo pInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        DLog.i("Plugin time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ApplicationInfo appInfo = pInfo.applicationInfo;
            /** 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            ActivityInfo[] activities = pInfo.activities;
            for (ActivityInfo activityInfo : activities) {
                DLog.i("Plugin activityInfo>" + activityInfo.name + " packageName: "+activityInfo.packageName );
            }
        }
    }

    public static File dealSo() {
        File ndkOutputDir = sContext.getDir("ndk", Activity.MODE_PRIVATE);
        PluginNativeLibsHelper.install(sApkFile.getAbsolutePath(), ndkOutputDir);
        File[] files = ndkOutputDir.listFiles();
        if (files != null) {
            for (File f : files)
                DLog.i("files>" + f.toString());
        }
        return ndkOutputDir;
    }

}
