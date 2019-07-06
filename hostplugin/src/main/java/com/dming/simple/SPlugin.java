package com.dming.simple;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import com.dming.simple.plugin.AppPlugin;
import com.dming.simple.plugin.activity.ActPitEvent;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.plugin.provider.ProPitEvent;
import com.dming.simple.plugin.provider.ProPlugin;
import com.dming.simple.plugin.receiver.RecPlugin;
import com.dming.simple.plugin.service.ServicePitEvent;
import com.dming.simple.plugin.service.ServicePlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.FileUtils;
import dalvik.system.DexClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class SPlugin {

    private static volatile SPlugin sSPlugin;
    private ClassLoader mClassLoader;
    private boolean mLoadPlugin = false;
    private String mHostPkgName;

    public boolean isLoadPlugin() {
        return mLoadPlugin;
    }

    private void setLoadPlugin(boolean mLoadPlugin) {
        this.mLoadPlugin = mLoadPlugin;
    }

    public static SPlugin getInstance() {
        if (sSPlugin == null) {
            synchronized (SPlugin.class) {
                if (sSPlugin == null) {
                    sSPlugin = new SPlugin();
                }
            }
        }
        return sSPlugin;
    }

    public static void initPlugin(final Context context, final String assetName, final OnPluginInitListener onPluginInitListener) {
        final SPlugin sPlugin = getInstance();
        sPlugin.initPlugin(context, onPluginInitListener, new PluginRunnable() {
            @Override
            public File getApkFile() throws IOException {
                return sPlugin.getPluginApkFromAsset(context, assetName);
            }
        });
    }

    public static void initPlugin(final Context context, final File pluginApkFile, final OnPluginInitListener onPluginInitListener) {
        final SPlugin sPlugin = getInstance();
        sPlugin.initPlugin(context, onPluginInitListener, new PluginRunnable() {
            @Override
            public File getApkFile() throws IOException {
                return sPlugin.getPluginApkFromFile(context, pluginApkFile);
            }
        });
    }

    private void initPlugin(final Context context, final OnPluginInitListener onPluginInitListener, final PluginRunnable pluginRunnable) {
        if (!isLoadPlugin()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File apkFile = pluginRunnable.getApkFile();
                        dealPlugin(context, apkFile);
                        setLoadPlugin(true);
                    } catch (Exception e) {
                        setLoadPlugin(false);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (onPluginInitListener != null) {
                                if (isLoadPlugin()) {
                                    onPluginInitListener.onSuccess();
                                } else {
                                    onPluginInitListener.onFailure();
                                }
                            }
                        }
                    });
                }
            });
            t.setDaemon(true);
            t.start();
        }else {
            if (isLoadPlugin()) {
                onPluginInitListener.onSuccess();
            } else {
                onPluginInitListener.onFailure();
            }
        }
    }

    public ClassLoader getClassLoader() {
        return mClassLoader;
    }

    private void dealPlugin(Context context, File apkFile) throws PackageManager.NameNotFoundException,
            ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        File ndkOutputDir = dealSo(context, apkFile);
        File dexOutputDir = context.getDir("dex", Activity.MODE_PRIVATE);
        mClassLoader = new DexClassLoader(apkFile.getAbsolutePath(),// dexPath
                dexOutputDir.getAbsolutePath(),// optimizedDirectory
                ndkOutputDir.getAbsolutePath(),
                context.getClassLoader().getParent()
        );
        dealHostPkgInfo(context);
        dealPluginPkgInfo(context, apkFile);
    }

    private void dealHostPkgInfo(Context context) {
        this.mHostPkgName = context.getPackageName();
    }

    public String getHostPkgName() {
        return mHostPkgName;
    }

    private void dealPluginPkgInfo(Context context, File apkFile) throws ClassNotFoundException,
            PackageManager.NameNotFoundException, NoSuchFieldException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String apkPath = apkFile.getAbsolutePath();
        long time = System.currentTimeMillis();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pInfo = packageManager.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_RECEIVERS |
                        PackageManager.GET_PROVIDERS);
        DLog.i("SPlugin time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ApplicationInfo appInfo = pInfo.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            Resources resource = packageManager.getResourcesForApplication(appInfo);
            Class<?> pluginClass = mClassLoader.loadClass("com.dming.simple.PluginManager");
            Field resources = pluginClass.getDeclaredField("sResources");
            Field applicationInfo = pluginClass.getDeclaredField("sApplicationInfo");
            Field classLoader = pluginClass.getDeclaredField("sClassLoader");
            pluginClass.getDeclaredMethod("setPicEvent", Object.class, Object.class, Object.class)
                    .invoke(null, ActPitEvent.getInstance(), ServicePitEvent.getInstance(), ProPitEvent.getInstance());
            resources.set(null, resource);
            applicationInfo.set(null, appInfo);
            classLoader.set(null, mClassLoader);
            AppPlugin.dealPluginApp(pInfo);
            ActPlugin.obtainPluginActivity(pInfo);
            ServicePlugin.obtainPluginService(pInfo);
            RecPlugin.dealPluginReceiver(context, pInfo, resource);
            ProPlugin.dealPluginProvider(pInfo);
        }
    }

    private File dealSo(Context context, File apkFile) {
        File ndkOutputDir = context.getDir("ndk", Activity.MODE_PRIVATE);
        PluginNativeLibsHelper.install(apkFile.getAbsolutePath(), ndkOutputDir);
        File[] files = ndkOutputDir.listFiles();
        if (files != null) {
            for (File f : files)
                DLog.i("files>" + f.toString());
        }
        return ndkOutputDir;
    }

    private File getPluginApkFromAsset(Context context, String assetName) throws IOException {
        File apkFile = new File(context.getFilesDir(), "dex_" + assetName);
        InputStream inputStream = context.getAssets().open(assetName);
        FileUtils.copyInputStreamToFile(inputStream, apkFile);
        return apkFile;
    }

    private File getPluginApkFromFile(Context context, File apkFile) throws IOException {
        File destApkFile = new File(context.getFilesDir(), "dex_" + apkFile.getName());
        FileUtils.copyDir(apkFile, destApkFile);
        return destApkFile;
    }

    public interface PluginRunnable {
        File getApkFile() throws IOException;
    }

    public static void unUseToClear(Context context) {
        ActPlugin.clear();
        ProPlugin.clear();
        RecPlugin.clear(context);
        ServicePlugin.clear();
    }

}
