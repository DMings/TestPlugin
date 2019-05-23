package com.dming.simple.plugin.load;

import android.app.Activity;
import android.content.Context;
import android.content.pm.*;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.plugin.activity.IPitActEvent;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

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
                                "setDealDexActivity", null, new Class[]{IPitActEvent.class}, ActPlugin.sPitActEvent);
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
        if (sPlugClassLoader != null) {
            String activity = ActPlugin.solveActClass(className);
            if(activity != null){
                try {
                    clazz = sPlugClassLoader.loadPluginClass(activity);
                } catch (ClassNotFoundException e) {
                    DLog.i("PlugClassLoader: " + e.getException());
                }
            }
        }
        return clazz;
    }


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

    public static void initHostPkgInfo() {
        long time = System.currentTimeMillis();
        PackageManager packageManager = sContext.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = packageManager.getPackageInfo(sContext.getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DLog.i("host time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ActPlugin.androidManifestActivityPit(pInfo);
            ServiceInfo[] services = pInfo.services;
            for (ServiceInfo serviceInfo : services) {
                DLog.i("Host ServiceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            }
        }
    }

    public static void getPluginInfo() {
        String apkPath = sApkFile.getAbsolutePath();
        long time = System.currentTimeMillis();
        PackageManager packageManager = sContext.getPackageManager();
        PackageInfo pInfo = packageManager.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_RECEIVERS |
                        PackageManager.GET_PROVIDERS);
        DLog.i("Plugin time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ApplicationInfo appInfo = pInfo.applicationInfo;
            /** 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            ActivityInfo[] activities = pInfo.activities;
            for (ActivityInfo activityInfo : activities) {
                DLog.i("Plugin ActivityInfo>" + activityInfo.name + " " + activityInfo.flags);
            }
            ServiceInfo[] services = pInfo.services;
            for (ServiceInfo serviceInfo : services) {
                DLog.i("Plugin ServiceInfo>" + serviceInfo.name + " " + serviceInfo.flags);
            }
            ActivityInfo[] receivers = pInfo.receivers;
            for (ActivityInfo receiverInfo : receivers) {
                DLog.i("Plugin ActivityInfo receivers>" + receiverInfo.name + " " + receiverInfo.flags);
//                Class
//                BroadcastReceiverDispatch.addSubBroadcastReceiver(new SubBroadcastReceiver());
            }
            ProviderInfo[] providers = pInfo.providers;
            for (ProviderInfo providerInfo : providers) {
                DLog.i("Plugin ProviderInfo>" + providerInfo.authority + " -> " + providerInfo.name);
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
